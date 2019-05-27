package ru.lfdesigns.contacts.ui.coordinators

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsFlowCoordinator @Inject constructor(): FlowCoordinator<ContactsNavigator>() {

    fun showContactById(id : Int){
        navigator?.showContactById(id)
    }

}