package ru.lfdesigns.contacts.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.model.*
import javax.inject.Inject

class ContactsViewModel @Inject constructor(private val repository: ContactsRepository
) : ViewModel() {

    var contacts: LiveData<PagedList<Contact>> = repository.contacts()

    var loadingStatus: LiveData<LoadingStatus> = repository.loadingStatus()

    fun refreshContacts() {
        repository.refreshContacts()
    }

    fun clearError() {
        repository.clearError()
    }

    fun setSearchTerm(query: String?) {
        repository.setContactsQuery(query)
        contacts.value?.dataSource?.invalidate()
    }

}