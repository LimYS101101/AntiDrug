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
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var loginBtn : Button
    private lateinit var sigupBtn : Button

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn = findViewById(R.id.login)
        sigupBtn = findViewById(R.id.signup)


        loginBtn.setOnClickListener{
            val id = gmail.text.toString()
            val pass = LoginPassword.text.toString()

            if (id == "" || pass == "") {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show()
            }

            else {
                findProfile(id, pass)
            }
        }

        sigupBtn.setOnClickListener{
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)

        }
    }

    private fun findProfile(id: String, pass: String){

        rootRef.child("Profile").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val prof = dataSnapshot.getValue(Profile::class.java)

                    if(prof!!.password == pass){

                        var prefs = getSharedPreferences("INFO", Context.MODE_PRIVATE)
                        val editor = prefs.edit()

                        editor.putString("Id", dataSnapshot.key)
                        editor.apply()

                        val intent = Intent(this@MainActivity, profileActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Incorrect Password", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    Toast.makeText(this@MainActivity, "User Doesn't Exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
