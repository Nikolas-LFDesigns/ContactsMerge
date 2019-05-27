package ru.lfdesigns.contacts.ui.coordinators

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactDetailFlowCoordinator @Inject constructor(): FlowCoordinator<ContactDetailNavigator>() {

    fun returnToContacts(){
        navigator?.returnToContacts()
    }

    fun dialNumber(number: String) {
        navigator?.dialNumber(number)
    }

}