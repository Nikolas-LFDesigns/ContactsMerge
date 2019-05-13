package ru.lfdesigns.contacts.api

import io.reactivex.Flowable
import ru.lfdesigns.contacts.model.Contact

/**
 * Converts a server API data to the user-facing data
 */
class ContactsUserApi(private val api: ContactsApi) {

    fun contacts(): Flowable<List<Contact>> {
        return Flowable.concat(
            api.contactsFirstSource().toFlowable(),
            api.contactsSecondSource().toFlowable(),
            api.contactsThirdSource().toFlowable()
        ).take(3)
    }

}