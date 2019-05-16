package ru.lfdesigns.contacts.db

import androidx.paging.DataSource
import androidx.paging.PagedList
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.query.PagedListLiveDataFactory
import ru.lfdesigns.contacts.query.PagedListLiveDataFactoryConverter
import java.util.concurrent.Executor

/**
 * Creates a data source abstracting search results from observer
 */
class ContactsSearchDataSourceFactory(private val dao: ContactDao) :
    DataSource.Factory<Int, Contact>(), PagedListLiveDataFactoryConverter<ContactsSearchDataSourceFactory, Int, Contact> {

    var query: String? = null

    override fun create(): DataSource<Int, Contact> {
        return if (query.isNullOrEmpty())
            dao.loadContacts().create()
        else
            dao.findContacts(query!!).create()
    }

    override fun toLiveDataFactory(
        config: PagedList.Config,
        initialLoadKey: Int?,
        boundaryCallback: PagedList.BoundaryCallback<Contact>?,
        fetchExecutor: Executor
    ): PagedListLiveDataFactory<ContactsSearchDataSourceFactory, Int, Contact> {
        return PagedListLiveDataFactory(this, config, initialLoadKey, boundaryCallback, fetchExecutor)
    }

    override fun toLiveDataFactory(
        pageSize: Int,
        initialLoadKey: Int?,
        boundaryCallback: PagedList.BoundaryCallback<Contact>?,
        fetchExecutor: Executor
    ): PagedListLiveDataFactory<ContactsSearchDataSourceFactory, Int, Contact> {
        return PagedListLiveDataFactory(this, pageSize, initialLoadKey, boundaryCallback, fetchExecutor)
    }
}