package ru.lfdesigns.contacts.arch

import androidx.navigation.NavController
import ru.lfdesigns.contacts.ui.ContactsFragmentDirections

class ContactsInteractor {

    lateinit var navController: NavController

    fun handleContactChoice(id: Int) {
        navController.navigate(ContactsFragmentDirections.showDetailAction(id))
    }
}