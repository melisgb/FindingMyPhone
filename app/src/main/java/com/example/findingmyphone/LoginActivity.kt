package com.example.findingmyphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            registerUser(editTxt_phoneLogin.text.toString())
        }

    }


    fun registerUser(phoneNumber : String){
        val userData =  UserData(applicationContext)
        userData.savePhoneNumber(phoneNumber)
        Log.d("User Info", phoneNumber)
        finish()
    }
}
