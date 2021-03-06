package ru.lfdesigns.contacts.db

import androidx.lifecycle.LiveData
import ru.lfdesigns.contacts.model.Contact
import androidx.paging.DataSource
import androidx.room.*


@Dao
abstract class ContactDao {

    @Insert
    abstract fun insert(contact: Contact): Long

    @Update
    abstract fun update(contact: Contact): Int

    @Transaction
    open fun insertOrUpdate(contacts: List<Contact>) {
        contacts.forEach { serverContact ->
            val contact = serverContact(serverContact.id)
            contact?.let {
                serverContact.localId = it.localId // to comply with id already on database
                update(serverContact)
            } ?: insert(serverContact)
        }
    }

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    abstract fun loadContacts(): DataSource.Factory<Int, Contact>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :search || '%' OR phone_normalized LIKE '%' || :search || '%' ORDER BY name ASC")
    abstract fun findContacts(search: String): DataSource.Factory<Int, Contact>

    @Query("SELECT * FROM contacts WHERE rowid = :id")
    abstract fun contact(id: Int): LiveData<Contact>

    @Query("SELECT * FROM contacts WHERE server_id = :id")
    abstract fun serverContact(id: String): Contact?
}