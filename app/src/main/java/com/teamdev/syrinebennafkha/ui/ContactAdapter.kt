package com.teamdev.syrinebennafkha.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.teamdev.syrinebennafkha.data.ContactEntity
import com.teamdev.syrinebennafkha.databinding.ItemContactBinding
import com.teamdev.syrinebennafkha.utils.ContactDiffCallback
import java.util.Locale

class ContactAdapter :
    ListAdapter<ContactEntity, ContactAdapter.ContactViewHolder>(ContactDiffCallback()),
    Filterable {

    private var contactList = listOf<ContactEntity>()
    private var contactListFiltered = listOf<ContactEntity>()

    fun setContacts(contacts: List<ContactEntity>) {
        contactList = contacts
        contactListFiltered = contacts
        submitList(contactListFiltered)
    }

    class ContactViewHolder(val binding: ItemContactBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactListFiltered[position]
        holder.binding.nameTextView.text = contact.name
        holder.binding.phoneTextView.text = contact.phoneNumber
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
                submitList(contactListFiltered)
            }
        }
    }
}
