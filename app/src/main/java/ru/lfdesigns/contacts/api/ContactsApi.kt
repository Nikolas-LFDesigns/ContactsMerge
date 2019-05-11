package ru.lfdesigns.contacts.api

import io.reactivex.Single
import retrofit2.http.GET
import ru.lfdesigns.contacts.model.Contact

interface ContactsApi {
    @GET("generated-01.json")
    fun contactsFirstSource(): Single<List<Contact>>

    @GET("generated-02.json")
    fun contactsSecondSource(): Single<List<Contact>>

    @GET("generated-03.json")
    fun contactsThirdSource(): Single<List<Contact>>
}