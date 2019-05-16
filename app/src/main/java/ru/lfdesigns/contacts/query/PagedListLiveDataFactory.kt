package ru.lfdesigns.contacts.query

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import java.util.concurrent.Executor

/**
 * Wrapper class for [DataSource.Factory.toLiveData] extension
 * functions, allows to create live data from typed namespace
 * and bake in a factory which created it
 */
class PagedListLiveDataFactory<Factory: DataSource.Factory<Key, Value>, Key, Value>(
    val backingFactory: Factory,
    private val config: PagedList.Config,
    private val initialLoadKey: Key? = null,
    private val boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    private val fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
)
    : LiveDataFactory<PagedList<Value>>() {

    constructor(backingFactory: Factory,
                pageSize: Int,
                initialLoadKey: Key? = null,
                boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
                fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
        ) : this(backingFactory, PagedList.Config.Builder().setPageSize(pageSize).build(), initialLoadKey, boundaryCallback, fetchExecutor )

    override fun toLiveData(): LiveData<PagedList<Value>> {
        return backingFactory.toLiveData(config, initialLoadKey, boundaryCallback, fetchExecutor)
    }
}
