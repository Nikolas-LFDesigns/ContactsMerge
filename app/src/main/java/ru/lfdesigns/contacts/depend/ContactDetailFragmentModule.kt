package ru.lfdesigns.contacts.depend

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.arch.*
import ru.lfdesigns.contacts.ui.ContactDetailFragment
import ru.lfdesigns.contacts.ui.coordinators.ContactDetailFlowCoordinator
import ru.lfdesigns.contacts.ui.coordinators.ContactDetailNavigator

@Module
class ContactDetailFragmentModule {

    @Provides
    internal fun provideContactDetailViewModelFactory(repository: ContactsRepository,
                                                      contactDetailFlowCoordinator: ContactDetailFlowCoordinator
    )
            : ContactDetailViewModelFactory {
        return ContactDetailViewModelFactory(repository,
            contactDetailFlowCoordinator::returnToContacts,
            contactDetailFlowCoordinator::dialNumber)
    }


    @ContactDetailScope
    @Provides
    @IntoMap
    @ClassKey(ContactDetailFragment::class)
    internal fun provideContactDetailFlowCoordinator()
            : ContactDetailFlowCoordinator {
        return ContactDetailFlowCoordinator()
    }

    @ContactDetailScope
    @Provides
    internal fun provideContactDetailNavigator(fragment: Fragment)
            : ContactDetailNavigator {
        return ContactDetailNavigator(fragment)
    }

}

@Module
abstract class ContactDetailFragmentBindingModule {

    @ContactDetailScope
    @Binds
    abstract fun getContactDetailFragmentAsFragment(fragment: ContactDetailFragment):
            Fragment

}
