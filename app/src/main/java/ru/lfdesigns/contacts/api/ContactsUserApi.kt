package ru.lfdesigns.contacts.api

import io.reactivex.Flowable
import ru.lfdesigns.contacts.model.Contact
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Converts a server API data to the user-facing data
 */
@Singleton
class ContactsUserApi @Inject constructor(private val api: ContactsApi) {

    fun contacts(): Flowable<List<Contact>> {
        return Flowable.concat(
            api.contactsFirstSource().toFlowable(),
            api.contactsSecondSource().toFlowable(),
            api.contactsThirdSource().toFlowable()
        ).take(3)
    }

}