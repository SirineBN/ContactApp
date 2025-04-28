package com.teamdev.syrinebennafkha.ui

import com.teamdev.syrinebennafkha.R
import com.teamdev.syrinebennafkha.data.ContactEntity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.util.*

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {

    private var contactList = listOf<ContactEntity>()
    private var contactListFiltered = listOf<ContactEntity>()

    fun setContacts(contacts: List<ContactEntity>) {
        contactList = contacts
        contactListFiltered = contacts
        notifyDataSetChanged()
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameTextView)
        val phone: TextView = view.findViewById(R.id.phoneTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactListFiltered[position]
        holder.name.text = contact.name
        holder.phone.text = contact.phoneNumber
    }

    override fun getItemCount(): Int = contactListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.ROOT)
                val filtered = if (query.isNullOrEmpty()) {
                    contactList
                } else {
                    contactList.filter {
                        it.name.lowercase(Locale.ROOT).contains(query)
                    }
                }
                val results = FilterResults()
                results.values = filtered
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactListFiltered = results?.values as List<ContactEntity>
                notifyDataSetChanged()
            }
        }
    }
}
