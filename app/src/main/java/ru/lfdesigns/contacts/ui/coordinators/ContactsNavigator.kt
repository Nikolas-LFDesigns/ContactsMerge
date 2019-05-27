package ru.lfdesigns.contacts.ui.coordinators

import androidx.fragment.app.Fragment
import ru.lfdesigns.contacts.ui.ContactsFragmentDirections

/**
 * A navigation for Contacts view
 */
class ContactsNavigator(fragment: Fragment)
    : NavigatorWrapper(fragment) {

    fun showContactById(id: Int) {
        navigator.navigate(ContactsFragmentDirections.showDetailAction(id))
    }

}
