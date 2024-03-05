package com.example.phonebook.classes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.phonebook.R
import com.example.phonebook.classes.data.Contact

class ContactRecycler(private var contacts: MutableList<Contact>?, private val context: Context, private val contactsReloadListener: ContactsReloadListener) :
    RecyclerView.Adapter<ContactRecycler.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_row, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts?.get(position)

        if (contact != null) {
          holder.bind(contact, context);
        }

        holder.contactRow.setOnClickListener{
            if (contact != null) {
                showContactProfile(contact, holder.contactRow)
            }
        }
    }

    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    fun showContactProfile(contact: Contact, someView: View) {
        val popupHelper = PopupHelper(context, contacts, this@ContactRecycler)

        popupHelper.showContactPopUp(someView, contact)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: MutableList<Contact>?) {
        if (filteredList != null) {
            contacts = filteredList.toMutableList()
        }

        notifyDataSetChanged()
    }

    fun removeContact(phoneNumber: String) {
        if (contacts != null) {
            for (contact in contacts!!) {
                if (contact.phoneNumber == phoneNumber) {
                    contacts!!.remove(contact)
                    filterList(contacts)

                    //notifying activity to reload the contacts list for correct filtering
                    contactsReloadListener.onContactsReload()
                    break
                }
            }
        }
    }

    fun updateContact(phoneNumber: String, newContact: Contact) {
        if (contacts != null) {
            for (contact in contacts!!) {
                if (contact.phoneNumber == phoneNumber) {
                    contact.firstName = newContact.firstName
                    contact.lastName = newContact.lastName
                    contact.phoneNumber = newContact.phoneNumber

                    filterList(contacts)

                    break
                }
            }
        }
    }

    fun addContact(contact: Contact) {
        contacts?.add(contact)

        filterList(contacts)
    }



    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameTextView: TextView = itemView.findViewById(R.id.contactNameTv)
        val phoneImageView: ImageView = itemView.findViewById(R.id.phoneIv)
        val contactRow: ConstraintLayout = itemView.findViewById(R.id.contactRowCl)

        fun bind(contact: Contact, context: Context) {
            fullNameTextView.text = contact.fullName
            fullNameTextView.visibility = View.VISIBLE

            phoneImageView.setOnClickListener{
                openDialerWithPhoneNumber(context, contact.phoneNumber)
            }
        }

        private fun openDialerWithPhoneNumber(context: Context, phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
        }

    }
}