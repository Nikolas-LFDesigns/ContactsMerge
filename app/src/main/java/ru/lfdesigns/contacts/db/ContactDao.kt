package ru.lfdesigns.contacts.db

import ru.lfdesigns.contacts.model.Contact
import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Single


@Dao
abstract class ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(contacts: List<Contact>): LongArray

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(contacts: List<Contact>): Int

    @Transaction
    open fun insertOrUpdate(contacts: List<Contact>) {
        insert(contacts)
        update(contacts)
    }

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    abstract fun loadContacts(): DataSource.Factory<Int, Contact>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :search || '%' OR phone LIKE '%' || :search || '%' ORDER BY name ASC")
    abstract fun findContacts(search: String): DataSource.Factory<Int, Contact>

    @Query("SELECT * FROM contacts WHERE rowid = :id")
    abstract fun contact(id: Int): Single<Contact>
}