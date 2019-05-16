package ru.lfdesigns.contacts.query

import androidx.lifecycle.LiveData

abstract class LiveDataFactory<T> {
    abstract fun toLiveData(): LiveData<T>
}