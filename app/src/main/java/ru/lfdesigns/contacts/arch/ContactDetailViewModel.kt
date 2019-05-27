package ru.lfdesigns.contacts.arch

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.model.*
import ru.lfdesigns.contacts.query.Queryable
import javax.inject.Inject

class ContactDetailViewModel @Inject constructor(private val repository: ContactsRepository,
                                                 private var onReturnToContacts: (() -> Unit)?,
                                                 private var onDialNumber: ((String) -> Unit)?
) : ViewModel() {

    val contactDetail: LiveData<Contact> by lazy {
        queryable.data
    }

    private val queryable: Queryable<Int, Contact> by lazy {
        repository.contact()
    }

    override fun onCleared() {
        onReturnToContacts = null
        onDialNumber = null
        super.onCleared()
    }

    @SuppressLint("CheckResult")
    fun loadContactById(id: Int) {
        queryable.query = id
    }

    fun returnToContacts() {
        onReturnToContacts?.invoke()
    }

    fun dialNumber(number: String) {
        onDialNumber?.invoke(number)
    }

}