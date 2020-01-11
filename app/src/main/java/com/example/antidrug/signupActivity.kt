package com.example.antidrug

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*

class signupActivity : AppCompatActivity() {

    var rootRef = FirebaseDatabase.getInstance().reference

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Profile")

    private lateinit var register: Button
    private lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


    register = findViewById(R.id.button)
    back = findViewById(R.id.button2)

    register.setOnClickListener{
        val loginid = loginid.text.toString()
        val password = password.text.toString()
        val repassword = repassword.text.toString()
        val gmail = gmail.text.toString()
        val name = name.text.toString()

        if (loginid == "" || password == "" || repassword == "" || gmail == ""|| name == "") {
            Toast.makeText(this, "Invalid data Filled In", Toast.LENGTH_LONG).show()
        }
        else {
            if (password == repassword) {
                val newProf = Profile(password, gmail, name)
                readProfile(loginid, newProf)
            } else
                Toast.makeText(this, "Different Password", Toast.LENGTH_LONG).show()
        }
    }
        back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
     }
}
    fun saveProfile(id: String, profile: Profile){
        myRef.child(id).setValue(profile)

        Toast.makeText(this@signupActivity, "User Created", Toast.LENGTH_LONG).show()
    }

    private fun readProfile(id: String, profile: Profile){

        rootRef.child("Profile").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists())
                    saveProfile(id, profile)
                else {
                    Toast.makeText(this@signupActivity, "User Exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
