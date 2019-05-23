package ru.lfdesigns.contacts.model

import androidx.lifecycle.LiveData

interface StatusResponse {
    val status: LiveData<LoadingStatus>
}