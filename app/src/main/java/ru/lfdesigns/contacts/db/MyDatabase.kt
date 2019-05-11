package ru.lfdesigns.contacts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.lfdesigns.contacts.model.Contact
import ru.lfdesigns.contacts.model.RefreshStatus

@Database(entities = [Contact::class, RefreshStatus::class], version = 1)
@TypeConverters(DateConverter::class, TemperamentConverter::class)
abstract class MyDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao
    abstract fun refreshStatusDao(): RefreshStatusDao
}