package com.teamdev.syrinebennafkha.ui


import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdev.syrinebennafkha.R
import com.teamdev.syrinebennafkha.data.ContactEntity
import com.teamdev.syrinebennafkha.databinding.ActivityMainBinding
import com.teamdev.syrinebennafkha.utils.PermissionsHelper


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ContactViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactAdapter()
        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContacts.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        viewModel.allContacts.observe(this) { contacts ->
            adapter.setContacts(contacts)
        }

        if (!PermissionsHelper.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            PermissionsHelper.requestPermission(this, Manifest.permission.READ_CONTACTS, REQUEST_CONTACTS_PERMISSION)
        } else {
            loadContacts()
        }
    }

    private fun loadContacts() {
        viewModel.deleteAll()
        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                viewModel.insert(ContactEntity(name = name, phoneNumber = phone))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }
}

