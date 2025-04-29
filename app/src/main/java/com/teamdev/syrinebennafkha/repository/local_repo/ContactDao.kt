package com.teamdev.syrinebennafkha.repository.local_repo


import androidx.lifecycle.LiveData
import androidx.room.*
import com.teamdev.syrinebennafkha.data.ContactEntity

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): LiveData<List<ContactEntity>>

    @Query("DELETE FROM contacts")
    suspend fun deleteAll()
}
