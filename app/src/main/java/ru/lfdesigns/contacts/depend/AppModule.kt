package ru.lfdesigns.contacts.depend

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.ContactsRepositoryImpl
import ru.lfdesigns.contacts.MyApplication
import ru.lfdesigns.contacts.api.ContactsUserApi
import ru.lfdesigns.contacts.db.ContactDao
import ru.lfdesigns.contacts.db.ContactsSearchDataSourceFactory
import ru.lfdesigns.contacts.db.MyDatabase
import ru.lfdesigns.contacts.db.RefreshStatusDao
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    internal fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }

    @Provides
    internal fun provideContactsApi(context: Context): ContactsUserApi {
        return (context.applicationContext as MyApplication).contactsApi
    }

    @Singleton
    @Provides
    internal fun provideContactsRepository(api: ContactsUserApi,
                                           contactsFactory: ContactsSearchDataSourceFactory,
                                           contactDao: ContactDao,
                                           refreshStatusDao: RefreshStatusDao): ContactsRepository {
        return ContactsRepositoryImpl(api, contactsFactory, contactDao, refreshStatusDao)
    }

    @Provides
    internal fun provideContactsSearchDataSourceFactory(contactDao: ContactDao): ContactsSearchDataSourceFactory {
        return ContactsSearchDataSourceFactory(contactDao)
    }

    @Provides
    internal fun provideRefreshStatusDao(db: MyDatabase): RefreshStatusDao {
        return db.refreshStatusDao()
    }

    @Provides
    internal fun provideContactDao(db: MyDatabase): ContactDao {
        return db.contactDao()
    }

    @Provides
    internal fun provideDatabase(context: Context): MyDatabase {
        return (context.applicationContext as MyApplication).database
    }

}