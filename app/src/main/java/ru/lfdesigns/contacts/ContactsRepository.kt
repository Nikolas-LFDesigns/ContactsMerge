package ru.lfdesigns.contacts

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.reactivex.Single
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.LoadingStatus

interface ContactsRepository {
    fun loadingStatus(): LiveData<LoadingStatus>
    fun contacts(): LiveData<PagedList<Contact>>
    fun refreshContacts()
    fun clearError()
    fun contactById(id: Int): Single<Contact>
    fun setContactsQuery(query: String?)
}