package com.example.antidrug

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class profileActivity : AppCompatActivity() {

    private lateinit var changePassBtn : Button
    private lateinit var logoutBtn : Button

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        changePassBtn = findViewById(R.id.button4)
        logoutBtn = findViewById(R.id.button5)


        val prefs = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val cid = prefs.getString("Id","")
        if (cid != null) {
            setInfo(cid)
        }

        changePassBtn.setOnClickListener{
            val intent = Intent(this, changePassActivity::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener{
            val editor = prefs.edit()
            editor.putString("Id", prefs.getString("Guest",""))
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun setInfo(id: String){
        rootRef.child("Profile").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val prof = dataSnapshot.getValue(Profile::class.java)

                    textView4.text = dataSnapshot.key
                    textView2.text = prof!!.gmail
                    textView3.text = prof!!.name
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
