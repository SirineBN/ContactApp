package com.teamdev.syrinebennafkha.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdev.syrinebennafkha.data.ContactEntity
import com.teamdev.syrinebennafkha.databinding.ItemContactBinding
import com.teamdev.syrinebennafkha.utils.ContactDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ContactAdapter :
    ListAdapter<ContactEntity, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    private var originalList = listOf<ContactEntity>()
    private val searchQuery = MutableStateFlow("")
    private val adapterScope = CoroutineScope(Dispatchers.Main + Job())

    fun setContacts(contacts: List<ContactEntity>) {
        originalList = contacts
        submitList(contacts)
    }

    fun filter(query: String) {
        searchQuery.value = query
    }

    init {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                val filtered = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter { it.name.contains(query, ignoreCase = true) }
                }
                submitList(filtered)
            }
            .launchIn(adapterScope)
    }

    class ContactViewHolder(val binding: ItemContactBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.nameTextView.text = contact.name
        holder.binding.phoneTextView.text = contact.phoneNumber

        holder.binding.root.alpha = 0f
        holder.binding.root.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

}
