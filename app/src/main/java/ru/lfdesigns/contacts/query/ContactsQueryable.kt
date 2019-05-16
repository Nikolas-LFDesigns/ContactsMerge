package ru.lfdesigns.contacts.query

import androidx.paging.PagedList
import ru.lfdesigns.contacts.db.ContactsSearchDataSourceFactory
import ru.lfdesigns.contacts.model.Contact

/**
 * Allows to query contacts list either in whole or with a [query] filter
 * @constructor
 * @param contactsFactory ContactsSearchDataSourceFactory transformed by [PagedListLiveDataFactoryConverter]
 */
class ContactsQueryable(private val contactsFactory: PagedListLiveDataFactory<ContactsSearchDataSourceFactory, Int, Contact>)
    : Queryable<String, PagedList<Contact>>(contactsFactory.toLiveData()) {

    override fun queryChanged() {
        contactsFactory.backingFactory.query = query
        data.value?.dataSource?.invalidate()
    }

}