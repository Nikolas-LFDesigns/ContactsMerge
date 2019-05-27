package ru.lfdesigns.contacts.depend

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.lfdesigns.contacts.ui.ContactDetailFragment
import ru.lfdesigns.contacts.ui.MainActivity
import ru.lfdesigns.contacts.ui.ContactsFragment

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContactsScope
    @ContributesAndroidInjector(modules = [ContactsFragmentModule::class, ContactsFragmentBindingModule::class])
    abstract fun bindContactsFragment(): ContactsFragment

    @ContactDetailScope
    @ContributesAndroidInjector(modules = [ContactDetailFragmentModule::class, ContactDetailFragmentBindingModule::class])
    abstract fun bindContactDetailFragment(): ContactDetailFragment
}