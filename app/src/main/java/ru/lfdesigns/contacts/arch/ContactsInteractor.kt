package ru.lfdesigns.contacts.arch

import androidx.navigation.NavController
import ru.lfdesigns.contacts.ui.ContactsFragmentDirections

class ContactsInteractor {

    var navController: NavController? = null

    fun handleContactChoice(id: Int) {
        navController?.navigate(ContactsFragmentDirections.showDetailAction(id))
    }
}