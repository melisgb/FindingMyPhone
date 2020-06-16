package com.example.findingmyphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UserData {
    var context : Context? = null
    var sharedRef : SharedPreferences? = null

    constructor(context : Context){
        this.context = context
        this.sharedRef = context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    }

    fun savePhoneNumber(phone: String){
        val editor = sharedRef!!.edit()
        editor.putString("phoneNumber", phone)
        editor.commit()
    }

    fun getPhoneNumber() : String {
        val phoneNumber = sharedRef!!.getString("phoneNumber", "empty") //if empty, return "empty"

        if(phoneNumber.equals("empty")){
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }
        return phoneNumber!!
    }


    companion object{
        var trackers : MutableMap<String, String> = HashMap()
    }

}

