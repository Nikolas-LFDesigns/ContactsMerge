package ru.lfdesigns.contacts.depend

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.arch.ContactsViewModelFactory
import ru.lfdesigns.contacts.ui.ContactsFragment
import ru.lfdesigns.contacts.ui.coordinators.ContactsFlowCoordinator
import ru.lfdesigns.contacts.ui.coordinators.ContactsNavigator


@Module
class ContactsFragmentModule {

    @ContactsScope
    @Provides
    internal fun provideContactsViewModelFactory(repository: ContactsRepository,
                                                 contactsFlowCoordinator: ContactsFlowCoordinator)
            : ContactsViewModelFactory {
        return ContactsViewModelFactory(repository, contactsFlowCoordinator::showContactById)
    }

    @ContactsScope
    @Provides
    @IntoMap @ClassKey(ContactsFragment::class)
    internal fun provideContactsFlowCoordinator()
            : ContactsFlowCoordinator {
        return ContactsFlowCoordinator()
    }

    @ContactsScope
    @Provides
    internal fun provideContactsNavigator(fragment: Fragment)
            : ContactsNavigator {
        return ContactsNavigator(fragment)
    }

}

@Module
abstract class ContactsFragmentBindingModule {

    @ContactsScope
    @Binds
    abstract fun getContactsFragmentAsFragment(fragment: ContactsFragment):
        Fragment

}