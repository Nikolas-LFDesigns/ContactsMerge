package ru.lfdesigns.contacts.query

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.lfdesigns.contacts.db.ContactDao
import ru.lfdesigns.contacts.model.Contact

/**
 * A wrapper class for [ContactDao] which allows to dynamically
 * change backing live data according to a [query]
 */
class ContactByIdLiveDataFactory(private val contactDao: ContactDao): LiveDataFactory<Contact>() {

    private val query = MutableLiveData<Int>()

    fun setQuery(query: Int?) {
        this.query.value = query
    }

    override fun toLiveData(): LiveData<Contact> {
        return Transformations.switchMap(query) {
            if (it == null || it == 0) {
                MutableLiveData(Contact())
            } else {
                contactDao.contact(it)
            }
        }
    }

}