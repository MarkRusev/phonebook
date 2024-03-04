package com.example.phonebook.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.phonebook.classes.data.Contact

class DBManager(context: Context) {

    private val db: SQLiteDatabase =
        context.openOrCreateDatabase("PhoneNotebook", Context.MODE_PRIVATE, null);

    private val table1 = "Contacts"
   // private val table1 = "Contacts_v2"

    init {
        val createContactsQuery = "CREATE TABLE IF NOT EXISTS `$table1` " +
                "(`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`FirstName` TEXT NOT NULL, " +
                "`LastName` TEXT, " +
                "`PhoneNumber` TEXT NOT NULL)"

        execute(createContactsQuery);
    }

    fun add(contact: Contact) {
        val addContactQuery = "INSERT INTO $table1 (firstName, lastName, phoneNumber) VALUES " +
                "('${contact.firstName}', " +
                "'${contact.lastName}', " +
                "'${contact.phoneNumber}')"

        execute(addContactQuery);
    }

    fun update(contact: Contact) {
        val updateContactQuery = "UPDATE $table1 " +
                "SET firstName = '${contact.firstName}', " +
                "lastName = '${contact.lastName}', " +
                "phoneNumber = '${contact.phoneNumber}' " +
                "WHERE Id = ${contact.id}"

        execute(updateContactQuery);
    }

    fun delete(contactId: Int) {
        val deleteContactQuery = "DELETE FROM $table1 WHERE Id = $contactId"

        execute(deleteContactQuery)
    }


    @SuppressLint("Range")
    fun getContacts(): MutableList<Contact>? {
        val getContactsQuery = "SELECT * FROM $table1"
        var contacts: MutableList<Contact>? = null

        val cursor = db.rawQuery(getContactsQuery, null)

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                contacts = mutableListOf()
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("Id"))
                    val firstName = cursor.getString(cursor.getColumnIndex("FirstName"))
                    val lastName = cursor.getString(cursor.getColumnIndex("LastName"))
                    val phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"))
                    val contact = Contact(id, firstName, lastName, phoneNumber)
                    contacts?.add(contact)
                } while (cursor.moveToNext())
            }
        }

        return contacts
    }

    fun execute(query: String){
        db.execSQL(query);
    }

}