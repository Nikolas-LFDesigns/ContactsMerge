package ru.lfdesigns.contacts.depend

import dagger.Module
import dagger.Provides
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.arch.ContactsViewModelFactory
import ru.lfdesigns.contacts.arch.ContactsInteractor

@Module
class ContactsFragmentModule {

    @Provides
    internal fun provideContactsViewModelFactory(repository: ContactsRepository)
            : ContactsViewModelFactory {
        return ContactsViewModelFactory(repository)
    }


    @Provides
    internal fun provideContactsInteractor()
            : ContactsInteractor {
        return ContactsInteractor()
    }

}