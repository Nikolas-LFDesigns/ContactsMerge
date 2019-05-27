package ru.lfdesigns.contacts.ui.coordinators

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

/**
 * A navigation for Contact detail view
 */
class ContactDetailNavigator(private val fragment: Fragment)
    : NavigatorWrapper(fragment) {

    fun returnToContacts() {
        navigator.navigateUp()
    }

    fun dialNumber(number: String) {
        fragment.activity?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number}"))
            it.startActivity(intent)
        }
    }

}
