package com.example.findingmyphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UserData {
    var context : Context? = null
    var sharedRef : SharedPreferences? = null

    companion object{
        var trackers : MutableMap<String, String> = HashMap()

        fun formatPhoneNum(phoneNumber: String) : String {
            //this function is to replace the phoneNumber into a phone with different format.
            val re = Regex("[^0-9]")
            var justNumber = re.replace(phoneNumber, "")

//            if(phoneNumber[0] == '+'){
//                justNumber = "+" + justNumber
//            }
            return justNumber
        }

    }


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

        return phoneNumber!!
    }

    fun saveContactInfo(){
        var listOfTrackers = ""
        for((key, value) in trackers){
            if(listOfTrackers.length == 0){
                listOfTrackers = key + "%" + value
            }
            else{
                listOfTrackers += "%" + key + "%" + value
            }
        }

        if(listOfTrackers.length == 0){
            listOfTrackers = "empty"
        }

        val editor = sharedRef!!.edit()
        editor.putString("listOfTrackers", listOfTrackers)
        editor.commit()
    }

    fun loadContactInfo(){
        trackers.clear()
        var listOfTrackers = sharedRef!!.getString("listOfTrackers", "empty")
        if(!listOfTrackers.equals("empty")){
            val usersInfo = listOfTrackers!!.split("%").toTypedArray()

            for(i in 0..usersInfo.size-2 step 2){
                trackers.put(usersInfo[i], usersInfo[i+1])
            }

        }

    }


}

