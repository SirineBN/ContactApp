package com.teamdev.syrinebennafkha.ui

import android.app.Application
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.teamdev.syrinebennafkha.data.ContactEntity
import com.teamdev.syrinebennafkha.repository.local_repo.ContactDatabase
import com.teamdev.syrinebennafkha.repository.local_repo.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val allContacts: LiveData<List<ContactEntity>>

    init {
        val dao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun insert(contact: ContactEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun loadContactsFromPhone(contentResolver: ContentResolver) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll() // ✅ Vide la base avant de recharger

            val contacts = mutableListOf<ContactEntity>()

            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contacts.add(ContactEntity(name = name, phoneNumber = phone))
                }
            }

            repository.insertAll(contacts) // ✅ Insert tous les contacts en une seule fois
        }

}
