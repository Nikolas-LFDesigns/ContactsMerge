package ru.lfdesigns.contacts

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.LoadingStatus
import ru.lfdesigns.contacts.query.Queryable

interface ContactsRepository {
    /**
     * Gets observable contacts loading status
     */
    fun loadingStatus(): LiveData<LoadingStatus>

    /**
     * Gets observable contacts list
     */
    fun contacts(): Queryable<String, PagedList<Contact>>

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
    fun contact(): Queryable<Int, Contact>

}