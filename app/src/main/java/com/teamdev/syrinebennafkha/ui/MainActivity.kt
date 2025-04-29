package com.teamdev.syrinebennafkha.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamdev.syrinebennafkha.databinding.ActivityMainBinding
import com.teamdev.syrinebennafkha.utils.PermissionsHelper
import com.teamdev.syrinebennafkha.utils.PermissionsHelper.REQUEST_CONTACTS_PERMISSION


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactAdapter
    private val viewModel: ContactViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        observeContacts()

        checkPermissionsAndLoadContacts()
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter()
        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContacts.adapter = adapter
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
    }

    private fun observeContacts() {
        viewModel.allContacts.observe(this) { contacts ->
            adapter.setContacts(contacts)

            if (contacts.isEmpty()) {
                binding.recyclerViewContacts.visibility = View.GONE
            } else {
                binding.recyclerViewContacts.visibility = View.VISIBLE
            }

        }
    }




    private fun checkPermissionsAndLoadContacts() {
        if (PermissionsHelper.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            viewModel.loadContactsFromPhone(contentResolver)
        } else {
            PermissionsHelper.requestPermission(
                this,
                Manifest.permission.READ_CONTACTS,
                REQUEST_CONTACTS_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadContactsFromPhone(contentResolver)
        }
    }
}
