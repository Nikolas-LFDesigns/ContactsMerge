package ru.lfdesigns.contacts.arch

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lfdesigns.contacts.ContactsRepository
import ru.lfdesigns.contacts.model.*
import javax.inject.Inject

class ContactDetailViewModel @Inject constructor(private val repository: ContactsRepository) : ViewModel() {

    private var _contactDetail: MutableLiveData<Contact> = MutableLiveData()
    var contactDetail: LiveData<Contact>
        get() = _contactDetail
        private set(_) {}


    @SuppressLint("CheckResult")
    fun loadContactById(id: Int) {
        repository.contactById(id)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                _contactDetail.value = it
            }, {}) // ignore synchronization-induced issues since we are viewing cached data
    }

}