package ru.lfdesigns.contacts.depend

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.ContactsRepositoryImpl
import ru.lfdesigns.contacts.MyApplication
import ru.lfdesigns.contacts.api.ContactsApi
import ru.lfdesigns.contacts.api.ContactsUserApi
import ru.lfdesigns.contacts.db.ContactDao
import ru.lfdesigns.contacts.db.ContactsSearchDataSourceFactory
import ru.lfdesigns.contacts.db.MyDatabase
import ru.lfdesigns.contacts.db.RefreshStatusDao
import ru.lfdesigns.contacts.model.PhoneNumber
import ru.lfdesigns.contacts.model.PhoneNumberTypeAdapter
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    internal fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideContactsApi(): ContactsApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(PhoneNumber::class.java, PhoneNumberTypeAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(ContactsApi::class.java)
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

    @Singleton
    @Provides
    internal fun provideDatabase(context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java, "contacts"
        ).build()
    }

}