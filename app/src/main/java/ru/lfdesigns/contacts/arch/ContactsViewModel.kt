package ru.lfdesigns.contacts.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.model.*
import ru.lfdesigns.contacts.query.Queryable
import javax.inject.Inject

class ContactsViewModel @Inject constructor(private val repository: ContactsRepository
) : ViewModel() {

    val contacts: LiveData<PagedList<Contact>> by lazy {
        queryable.data
    }

    private val queryable: Queryable<String, PagedList<Contact>> by lazy {
        repository.contacts()
    }

    var loadingStatus: LiveData<LoadingStatus> = repository.loadingStatus()

    fun refreshContacts() {
        repository.refreshContacts()
    }

    fun clearError() {
        repository.clearError()
    }

    fun setSearchTerm(query: String?) {
        queryable.query = query
    }

}