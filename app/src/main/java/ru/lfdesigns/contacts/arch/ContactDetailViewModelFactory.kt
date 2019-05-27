package ru.lfdesigns.contacts.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.lfdesigns.contacts.ContactsRepository

@Suppress("UNCHECKED_CAST")
class ContactDetailViewModelFactory(
    private val repository: ContactsRepository,
    private val onReturnToContacts: ( () -> Unit )?,
    private var onDialNumber: ((String) -> Unit)?
)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactDetailViewModel(
            repository,
            onReturnToContacts,
            onDialNumber
        ) as T
    }

}