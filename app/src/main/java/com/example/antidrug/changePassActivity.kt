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
import kotlinx.android.synthetic.main.activity_change_pass.*

class changePassActivity : AppCompatActivity() {

    private lateinit var changeP : Button
    private lateinit var back : Button

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        changeP = findViewById(R.id.confirmchange)
        back = findViewById(R.id.back)

        changeP.setOnClickListener{
            val oldpassword = oldpassword.text.toString()
            val newpassword = newpassword.text.toString()
            val reenternewpassword = reenternewpassword.text.toString()

            if (oldpassword == "" || newpassword == "" || reenternewpassword == "") {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show()
            }
            else {
                if(newpassword == reenternewpassword) {
                    updatePass(oldpassword, newpassword)
                }
                else
                    Toast.makeText(this, "Password Not Match", Toast.LENGTH_LONG).show()
            }
        }

        back.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    private fun updatePass(oldPass:String, newPass:String){

        var prefs = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val id = prefs.getString("Id","")

        if (id != null) {
            rootRef.child("Profile").child(id).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        val prof = dataSnapshot.getValue(Profile::class.java)

                        if(prof!!.password == oldPass){

                            rootRef.child("Profile").child(id).child("password").setValue(newPass)

                            Toast.makeText(this@changePassActivity, "Password Updated", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@changePassActivity, "Incorrect Password", Toast.LENGTH_LONG).show()
                        }
                    }
                    else {
                        Toast.makeText(this@changePassActivity, "User Doesn't Exist", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }
    }
}
