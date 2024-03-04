package com.example.phonebook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.phonebook.R
import com.example.phonebook.classes.PopupHelper

class MainActivity : AppCompatActivity() {

    private lateinit var phonebookCv: CardView
    private lateinit var addContactCv: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phonebookCv = findViewById(R.id.phonebookCv)
        addContactCv = findViewById(R.id.addContactCv)

        phonebookCv.setOnClickListener {
            val intent = Intent(this@MainActivity, ContactsActivity::class.java)
            startActivity(intent)
        }

        addContactCv.setOnClickListener{
            val popupHelper = PopupHelper(this@MainActivity, null,null)
            popupHelper.showContactPopUp(addContactCv, null)
        }

    }


}