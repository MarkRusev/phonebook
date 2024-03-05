package com.example.phonebook.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phonebook.classes.ContactsReloadListener
import com.example.phonebook.R
import com.example.phonebook.classes.data.Contact
import com.example.phonebook.classes.ContactRecycler
import com.example.phonebook.db.QueryExecutor
import java.util.Locale


class ContactsActivity : AppCompatActivity(), ContactsReloadListener {
    private lateinit var queryExecutor: QueryExecutor
    private var contacts: MutableList<Contact>? = null
    private lateinit var recycler: RecyclerView
    private lateinit var searchView: android.widget.SearchView
    private var isExpanded = false
    private lateinit var adapter: ContactRecycler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        //recycler
        recycler = findViewById(R.id.contactsRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        searchView = findViewById(R.id.searchView)
        queryExecutor = QueryExecutor(this)

        // Load contacts from database
        contacts = queryExecutor.contacts()

        // Create adapter and set it to the RecyclerView
        contacts?.let {
            adapter = ContactRecycler(contacts, this@ContactsActivity, this)
            recycler.adapter = adapter
        }

        searchViewHandler(searchView = searchView)
    }


    fun searchViewHandler(searchView: SearchView){

        //i make sure that if user click on the text input area it would be poss to write
        searchView.setOnClickListener {
            if (isExpanded) {
                searchView.isIconified = true // collapse the SearchView
            } else {
                searchView.isIconified = false // expand the SearchView
                searchView.requestFocus() // request focus to show keyboard
            }
            isExpanded = !isExpanded // toggle state
        }

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            isExpanded = hasFocus // Update the state based on focus
        }
        /////

        // Filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(input: String): Boolean {
                filter(input)
                return true
            }
        })
    }

    private fun filter(query: String) {
        val filteredList: MutableList<Contact> = mutableListOf()

        // preventing NullPointerException
        contacts?.let { contactList ->
            if (TextUtils.isEmpty(query)) {
                filteredList.addAll(contactList)
            } else {
                val filterPattern = query.lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (contact in contactList) {
                    if (contact.fullName.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(contact)
                    }
                    else if (contact.phoneNumber.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(contact)
                    }
                }
            }
        }
        adapter.filterList(filteredList)
    }

    override fun onContactsReload() {
        contacts = queryExecutor.contacts()
    }


}