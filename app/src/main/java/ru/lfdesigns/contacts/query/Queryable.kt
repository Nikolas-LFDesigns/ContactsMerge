package ru.lfdesigns.contacts.query

import androidx.lifecycle.LiveData

/**
 * An abstraction which allows to transparently filter live data
 * using an abstract query and backing live data transformers (such as MediatorLiveData)
 * which depend on that query
 */
abstract class Queryable<QueryKey, Value>(
    val data: LiveData<Value>
) {
    private var _query: QueryKey? = null
    var query: QueryKey?
    get() {
        return _query
    }
    set(value) {
        _query = value
        queryChanged()
    }

    protected abstract fun queryChanged()
}
