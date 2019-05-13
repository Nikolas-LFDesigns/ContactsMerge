package ru.lfdesigns.contacts

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.reactivex.Single
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.LoadingStatus

interface ContactsRepository {
    /**
     * Gets observable contacts loading status
     */
    fun loadingStatus(): LiveData<LoadingStatus>

    /**
     * Gets observable whole contacts list
     */
    fun contacts(): LiveData<PagedList<Contact>>

    /**
     * Force-refreshes contacts
     */
    fun refreshContacts()

    /**
     * Allows to restore state of data loading, e.g. loadingStatus, after error
     */
    fun clearError()

    /**
     * Asynchronously get single contact from storage
     */
    fun contactById(id: Int): Single<Contact>

    /**
     * Apply a search term to the resulting contacts set
     */
    fun setContactsQuery(query: String?)
}