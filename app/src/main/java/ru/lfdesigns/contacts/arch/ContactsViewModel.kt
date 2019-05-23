package ru.lfdesigns.contacts.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.model.*
import ru.lfdesigns.contacts.query.Queryable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ContactsViewModel @Inject constructor(private val repository: ContactsRepository
) : ViewModel() {

    val contacts: LiveData<PagedList<Contact>> by lazy {
        queryable.data
    }

    private val queryable: Queryable<String, PagedList<Contact>> by lazy {
        repository.contacts()
    }

    val loadingStatus: LiveData<LoadingStatus> by lazy {
        (queryable as StatusResponse).status
    }

    private val queryPublisher: PublishSubject<String> = PublishSubject.create()
    private val subscribers = CompositeDisposable()

    init {
        subscribers.add(
            queryPublisher.toFlowable(BackpressureStrategy.BUFFER)
                .doOnNext { query-> // remove empty query earlier to instantly show whole list
                    if (query.isNullOrEmpty())
                        queryable.query = null
                }
                .filter(String::isNotEmpty)
                .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
                .distinctUntilChanged()
                .switchMap {query->
                    Flowable.just(query);
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query ->
                    queryable.query = query
                }
        )
    }

    override fun onCleared() {
        subscribers.clear()
        super.onCleared()
    }

    fun refreshContacts() {
        repository.refreshContacts()
    }

    fun clearError() {
        repository.clearError()
    }

    fun setSearchTerm(query: String) {
        queryPublisher.onNext(query)
    }

}