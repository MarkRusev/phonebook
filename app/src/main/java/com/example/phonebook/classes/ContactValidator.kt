package com.example.phonebook.classes

import com.example.phonebook.classes.data.Contact

class ContactValidator(private val contacts: MutableList<Contact>?) {

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Regular expression to match a phone number format (10 digits)
        val regex = Regex("^\\d{10}$")

        // Check if the phone number matches the regular expression
        return regex.matches(phoneNumber)
    }

    fun phoneNumberExists(phoneNumber: String): Boolean {
        if (contacts != null) {
            for (contact in contacts) {
                if (contact.phoneNumber == phoneNumber) {
                    return true // Phone number already exists in contacts
                }
            }
        }
        return false
    }
}