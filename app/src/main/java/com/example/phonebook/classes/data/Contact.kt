package com.example.phonebook.classes.data

data class Contact(
    var id: Int = 1,
    var firstName: String,
    var lastName: String?,
    var phoneNumber: String
) {
    // will use it to make the text appending easier and the contact search too
    var fullName: String
        get() = "$firstName ${lastName ?: ""}"
        set(value) {
            val parts = value.split(" ", limit = 2)
            firstName = parts.first()
            lastName = parts.getOrNull(1)
        }

    override fun toString(): String {
        return "Contact(id=$id, firstName='$firstName', lastName=$lastName, phoneNumber='$phoneNumber')"
    }
}

