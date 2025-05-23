package com.teamdev.syrinebennafkha.repository.local_repo

import androidx.lifecycle.LiveData
import com.teamdev.syrinebennafkha.data.ContactEntity

class ContactRepository(private val dao: ContactDao) {

    val allContacts: LiveData<List<ContactEntity>> = dao.getAllContacts()

    suspend fun insert(contact: ContactEntity) {
        dao.insert(contact)
    }

    suspend fun insertAll(contacts: List<ContactEntity>) {
        dao.insertAll(contacts)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
