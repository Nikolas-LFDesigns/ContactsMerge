package ru.lfdesigns.contacts.query

import ru.lfdesigns.contacts.model.Contact

/**
 * Allows to query contact by id with a [query] filter
 * @constructor
 * @param contactFactory a factory encapsulating a query logic
 */
class ContactByIdQueryable(private val contactFactory: ContactByIdLiveDataFactory)
    : Queryable<Int, Contact>(contactFactory.toLiveData()) {

    override fun queryChanged() {
        contactFactory.setQuery(query)
    }

}