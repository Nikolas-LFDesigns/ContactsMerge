package ru.lfdesigns.contacts

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lfdesigns.contacts.api.ContactsUserApi
import ru.lfdesigns.contacts.db.ContactDao
import ru.lfdesigns.contacts.db.ContactsSearchDataSourceFactory
import ru.lfdesigns.contacts.db.RefreshStatusDao
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.LoadingStatus
import ru.lfdesigns.contacts.model.RefreshStatus
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ContactsRepositoryImpl @Inject constructor(private var api: ContactsUserApi,
                                                 private val contactsFactory: ContactsSearchDataSourceFactory,
                                                 private val contactDao: ContactDao,
                                                 private val refreshStatusDao: RefreshStatusDao) : ContactsRepository {

    private var _loadingStatus: MutableLiveData<LoadingStatus> = MutableLiveData(LoadingStatus.IDLE)

    override fun loadingStatus(): LiveData<LoadingStatus> {
        return _loadingStatus
    }

    @SuppressLint("CheckResult")
    override fun contacts(): LiveData<PagedList<Contact>> {
        getRefreshNeededSingle()
            .filter { it }
            .flatMapSingle { getSaveNewRefreshValueSingle() }
            .flatMap { getRefreshContactsSingle() }
            .observeOn(Schedulers.computation())
            .subscribeOn(Schedulers.computation())
            .subscribe ({}, Throwable::printStackTrace)
        return contactsFactory.toLiveData(pageSize = 20)
    }

    private fun getRefreshNeededSingle(): Single<Boolean> {
        return Flowable.create({ subscriber: FlowableEmitter<RefreshStatus> ->
            var status: RefreshStatus? = null
            try {
                status = refreshStatusDao.status()
            } catch (e: Exception) {}
            status?.let {
                subscriber.onNext(it)
                subscriber.onComplete()
            }?: subscriber.onComplete()
        }, BackpressureStrategy.MISSING)
            .take(1)
            .single(RefreshStatus(forContacts = Date(0)))
            .map { status ->
                val statusTime = Calendar.getInstance().apply {
                    time = status.forContacts
                    add(Calendar.MINUTE, 1)
                }
                return@map Calendar.getInstance().time.after(statusTime.time)
            }
    }

    private fun getSaveNewRefreshValueSingle(): Single<Int> {
        return Single.create {
            refreshStatusDao.insertOrUpdate(RefreshStatus(forContacts = Date()))
            it.onSuccess(1)
        }
    }

    private fun getRefreshContactsSingle(): Single<Int> {
        var count = 0
        return api.contacts()
            .doOnSubscribe { _loadingStatus.postValue(LoadingStatus.LOADING) }
            .doOnNext {
                contactDao.insertOrUpdate(it)
            }
            .doOnError { _loadingStatus.postValue(LoadingStatus.ERROR) }
            .doOnComplete {
                _loadingStatus.postValue(LoadingStatus.LOADED)
            }
            .collectInto(count, { _, list -> count += list.size})
    }

    @SuppressLint("CheckResult")
    override fun refreshContacts() {
        getRefreshContactsSingle()
            .flatMap { getSaveNewRefreshValueSingle() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Throwable::printStackTrace)
    }

    override fun clearError() {
        _loadingStatus.postValue(LoadingStatus.IDLE)
    }

    override fun contactById(id: Int): Single<Contact> {
        return contactDao.contact(id)
    }

    override fun setContactsQuery(query: String?) {
        contactsFactory.query = query
    }
}