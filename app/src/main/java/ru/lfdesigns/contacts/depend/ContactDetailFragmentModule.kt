package ru.lfdesigns.contacts.depend

import dagger.Module
import dagger.Provides
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.arch.*

@Module
class ContactDetailFragmentModule {

    @Provides
    internal fun provideContactDetailViewModelFactory(repository: ContactsRepository)
            : ContactDetailViewModelFactory {
        return ContactDetailViewModelFactory(repository)
    }

}