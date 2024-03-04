package com.example.phonebook.classes

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.phonebook.R
import com.example.phonebook.classes.data.Contact
import com.example.phonebook.db.QueryExecutor

class PopupHelper(
    private val context: Context,
    private val contacts: MutableList<Contact>?,
    private val contactRecycler: ContactRecycler?
) {
    private val queryExecutor = QueryExecutor(context)

    fun showContactPopUp(addContactCv: View, contact: Contact?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.contact_popup, null)

        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show the popup centered on the screen
        popupWindow.showAtLocation(addContactCv, Gravity.CENTER, 0, 0)

        // Set up any interaction with popup views here

        // Example: Dismiss the popup when a button inside the popup is clicked
        val closeTv = popupView.findViewById<TextView>(R.id.cancelTv)
        val doneTv = popupView.findViewById<TextView>(R.id.doneTv)
        val firstNameEt = popupView.findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEt = popupView.findViewById<EditText>(R.id.lastNameEditText)
        val phoneNumberEt = popupView.findViewById<EditText>(R.id.phoneNumberEditText)
        val deleteContactBtn = popupView.findViewById<Button>(R.id.deleteButton)

        if (contact != null) {
            firstNameEt.setText(contact.firstName)
            lastNameEt.setText(contact.lastName)
            phoneNumberEt.setText(contact.phoneNumber)
        }else{
            deleteContactBtn.visibility = View.GONE
        }

        closeTv.setOnClickListener { popupWindow.dismiss() }

        doneTv.setOnClickListener {
            val firstName = firstNameEt.text.toString().trim()
            val lastName = lastNameEt.text.toString().trim()
            val phoneNumber = phoneNumberEt.text.toString().trim()
            val contactValidator = ContactValidator(queryExecutor.contacts())

            if (firstName.isEmpty()) {
                Toast.makeText(context, R.string.firstNameEmpty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                Toast.makeText(context, R.string.phoneNumberEmpty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!contactValidator.isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(context, R.string.invalidPhoneNumber, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            if (contact != null) {
                //updating contact data
                val newContact = Contact(firstName = firstName, lastName = lastName, phoneNumber = phoneNumber)

                contactRecycler?.updateContact(contact.phoneNumber, newContact)

                queryExecutor.updateContact(contact)
            } else {

                if (contactValidator.phoneNumberExists(phoneNumber)) {
                    Toast.makeText(context, R.string.phoneNumberExists, Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val newContact = Contact(firstName = firstName, lastName = lastName, phoneNumber = phoneNumber)
                queryExecutor.addContact(newContact)

                contactRecycler?.addContact(newContact)
            }

            popupWindow.dismiss()
        }

        deleteContactBtn.setOnClickListener {
            contact?.id?.let {
                popupWindow.dismiss()
                showDeletingPopup(addContactCv, contact)
            }
        }
    }

    private fun showDeletingPopup(someView: View, contact: Contact) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.delete_double_check_popup, null)


        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        someView.post {
            popupWindow.showAtLocation(someView, Gravity.TOP, 0, 0)
        }


        val deleteContactBtn = popupView.findViewById<Button>(R.id.deleteButton)
        val cancelBtn = popupView.findViewById<Button>(R.id.cancelButton)

        cancelBtn.setOnClickListener {
            popupWindow.dismiss()
        }

        deleteContactBtn.setOnClickListener {
            //delete from db
            queryExecutor.deleteContact(contact.id)

            // remove dynamically from recycler and see the changes immediately
            contactRecycler?.removeContact(contact.phoneNumber)

            popupWindow.dismiss()

        }
    }

}