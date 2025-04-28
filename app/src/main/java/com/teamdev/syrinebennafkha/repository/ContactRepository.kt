package com.teamdev.syrinebennafkha.repository


import androidx.lifecycle.LiveData
import com.teamdev.syrinebennafkha.data.ContactDao
import com.teamdev.syrinebennafkha.data.ContactEntity


class ContactRepository(private val dao: ContactDao) {

    val allContacts: LiveData<List<ContactEntity>> = dao.getAllContacts()

    suspend fun insert(contact: ContactEntity) {
        dao.insert(contact)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
