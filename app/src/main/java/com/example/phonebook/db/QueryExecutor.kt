package com.example.phonebook.db

import android.content.Context
import com.example.phonebook.classes.data.Contact
import kotlinx.coroutines.runBlocking

class QueryExecutor(context: Context) {

    private val dbManager = DBManager(context);


    fun deleteContact(contactId: Int){
        dbManager.delete(contactId)
    }

    fun updateContact(contact: Contact){
        dbManager.update(contact)
    }

    fun addContact(contact: Contact){
        dbManager.add(contact)
    }

    fun contacts(): MutableList<Contact>?{
        var contacts: MutableList<Contact>? = null

        runBlocking {
            contacts = dbManager.getContacts();
        }

        return contacts;
    }

}