package com.example.findingmyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.action.equals("android.intent.action.BOOT_COMPLETED")){
            val intent = Intent(context, MyService::class.java)
            context!!.startService(intent)

        }
    }

}