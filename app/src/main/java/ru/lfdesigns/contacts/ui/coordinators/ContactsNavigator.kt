package ru.lfdesigns.contacts.ui.coordinators

import ru.lfdesigns.contacts.ui.ContactsFragmentDirections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsNavigator @Inject constructor()
    : NavigatorWrapper() {

    fun showContactById(id: Int) {
        navigator.navigate(ContactsFragmentDirections.showDetailAction(id))
    }

}
