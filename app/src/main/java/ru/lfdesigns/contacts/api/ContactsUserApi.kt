package ru.lfdesigns.contacts.api

import io.reactivex.Flowable
import io.reactivex.Single
import ru.lfdesigns.contacts.model.Contact

/**
 * Converts a server API data to the user-facing data
 */
class ContactsUserApi(private val api: ContactsApi) {

    fun contacts(): Single<ArrayList<Contact>> {
        val finalList: ArrayList<Contact> = arrayListOf()
        return Flowable.concat(
            api.contactsFirstSource().toFlowable(),
            api.contactsSecondSource().toFlowable(),
            api.contactsThirdSource().toFlowable()
        ).collectInto(finalList, { list, newList -> list.addAll(newList)})
    }

}