package ru.lfdesigns.contacts.query

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.DataSource
import androidx.paging.PagedList
import java.util.concurrent.Executor

/**
 * Interface for DataSource.Factory#toLiveData extension
 * functions, allows any factory to be converted to [LiveDataFactory]
 */
interface PagedListLiveDataFactoryConverter<Factory: DataSource.Factory<Key, Value>, Key, Value> {

    fun toLiveDataFactory(
        config: PagedList.Config,
        initialLoadKey: Key? = null,
        boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
        fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
    ): PagedListLiveDataFactory<Factory, Key, Value>

    fun toLiveDataFactory(
        pageSize: Int,
        initialLoadKey: Key? = null,
        boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
        fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
    ) : PagedListLiveDataFactory<Factory, Key, Value>

}