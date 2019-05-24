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
import ru.lfdesigns.contacts.ui.coordinators.NavigatorWrapper
import dagger.MapKey
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class ClassKey(val value: KClass<out Fragment>)

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
    internal fun provideContactsFlowCoordinator(navigator: ContactsNavigator)
            : ContactsFlowCoordinator {
        return ContactsFlowCoordinator(navigator)
    }

    @ContactsScope
    @Provides
    @IntoMap @ClassKey(ContactsFragment::class)
    internal fun provideContactsNavigator()
            : ContactsNavigator {
        return ContactsNavigator()
    }

}

@Module
abstract class ContactsFragmentBindingModule {
/*
    @ContactsScope
    @Binds
    abstract fun getContactsNavigatorAsNavigatorWrapper(contactsNavigator: ContactsNavigator):
            NavigatorWrapper*/

}