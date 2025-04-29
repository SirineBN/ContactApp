package com.teamdev.syrinebennafkha.utils

import androidx.recyclerview.widget.DiffUtil
import com.teamdev.syrinebennafkha.data.ContactEntity

class ContactDiffCallback : DiffUtil.ItemCallback<ContactEntity>() {
    override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
        return oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
        return oldItem == newItem
    }
}
