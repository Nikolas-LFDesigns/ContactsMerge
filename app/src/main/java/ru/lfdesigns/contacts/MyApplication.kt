package ru.lfdesigns.contacts

import android.app.Activity
import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.lfdesigns.contacts.api.ContactsApi
import javax.inject.Inject
import dagger.android.AndroidInjector
import ru.lfdesigns.contacts.api.ContactsUserApi
import ru.lfdesigns.contacts.db.MyDatabase
import ru.lfdesigns.contacts.depend.DaggerAppComponent


class MyApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var contactsApi: ContactsUserApi
    lateinit var database: MyDatabase

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this);

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        database = Room.databaseBuilder(
            applicationContext,
            MyDatabase::class.java, "contacts"
        ).build()

        contactsApi = ContactsUserApi(retrofit.create(ContactsApi::class.java))
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

}
