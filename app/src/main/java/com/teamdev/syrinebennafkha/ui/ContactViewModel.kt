package com.teamdev.syrinebennafkha.ui


import android.app.Application
import androidx.lifecycle.*
import com.teamdev.syrinebennafkha.data.ContactDatabase
import com.teamdev.syrinebennafkha.data.ContactEntity
import com.teamdev.syrinebennafkha.repository.ContactRepository

import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val allContacts: LiveData<List<ContactEntity>>

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts
    }

    fun insert(contact: ContactEntity) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
