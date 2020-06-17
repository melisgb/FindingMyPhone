package com.example.findingmyphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private var mAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        signAnonymously()

        btn_login.setOnClickListener {
            registerUser(editTxt_phoneLogin.text.toString())
        }

    }

    fun signAnonymously(){
        mAuth!!.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Firebase", "signInAnonymously:success")
                val user = mAuth!!.currentUser
            } else {
                // If sign in fails, display a message to the user.
                Log.d("Firebase", "signInAnonymously:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun registerUser(phoneNumber : String){
        val userData =  UserData(applicationContext)
        userData.savePhoneNumber(phoneNumber)
        Log.d("User Info", phoneNumber)

        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:MM:ss")
        val currDate = Date()
        val mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("Users").child(phoneNumber).child("request")
            .setValue(dateFormat.format(currDate).toString())
        finish()
    }
}
