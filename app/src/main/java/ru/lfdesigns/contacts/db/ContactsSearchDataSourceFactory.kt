package ru.lfdesigns.contacts.db

import androidx.paging.DataSource
import ru.lfdesigns.contacts.model.Contact

/**
 * Creates a data source abstracting search results from observer
 */
class ContactsSearchDataSourceFactory(private val dao: ContactDao) :
    DataSource.Factory<Int, Contact>() {

    var query: String? = null

    override fun create(): DataSource<Int, Contact> {
        return if (query.isNullOrEmpty())
            dao.loadContacts().create()
        else
            dao.findContacts(query!!).create()
    }
}