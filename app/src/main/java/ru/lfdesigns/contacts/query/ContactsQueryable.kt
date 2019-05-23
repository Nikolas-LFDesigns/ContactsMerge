package ru.lfdesigns.contacts.query

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import ru.lfdesigns.contacts.db.ContactsSearchDataSourceFactory
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.LoadingStatus
import ru.lfdesigns.contacts.model.StatusResponse

/**
 * Allows to query contacts list either in whole or with a [query] filter
 * @constructor
 * @param contactsFactory ContactsSearchDataSourceFactory transformed by [PagedListLiveDataFactoryConverter]
 */
class ContactsQueryable(private val contactsFactory: PagedListLiveDataFactory<ContactsSearchDataSourceFactory, Int, Contact>)
    : Queryable<String, PagedList<Contact>>(contactsFactory.toLiveData()), StatusResponse {

    private val _status = MutableLiveData<LoadingStatus>()
    override val status: LiveData<LoadingStatus> = _status

    fun setStatus(value: LoadingStatus) {
        _status.postValue(value)
    }

    override fun queryChanged() {
        contactsFactory.backingFactory.query = query
        data.value?.dataSource?.invalidate()
    }

}