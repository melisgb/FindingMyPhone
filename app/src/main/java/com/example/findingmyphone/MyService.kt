package com.example.findingmyphone

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MyService : Service(){
    var mDatabase : DatabaseReference? = null

    companion object{
        //everything inside here will be publicly accessed
        var isServiceRunning = false
    }
    override fun onBind(intent: Intent?): IBinder? {
        //when someone tries to start the service
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //when service is created
        mDatabase = FirebaseDatabase.getInstance().reference
        isServiceRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //this will run in Background
        var userLocation = MyLocation()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, userLocation)

        //everytime the request node is updated, run this (save the location and lastLogin Info into the node of this user).
        val userData  = UserData(this)
        val userPhone = userData.getPhoneNumber()

        mDatabase!!.child("Users").child(userPhone).child("login")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(userLocation == null) return

                    mDatabase!!.child("Users").child(userPhone).child("location")
                        .child("lat").setValue(userLocation.myLocation!!.latitude)
                    mDatabase!!.child("Users").child(userPhone).child("location")
                        .child("long").setValue(userLocation.myLocation!!.longitude)

                    //get time
                    val dateForm= SimpleDateFormat("yyyy/MM/dd HH:MM:ss")
                    val date = Date()
                    mDatabase!!.child("Users").child(userPhone).child("location")
                        .child("lastSession").setValue(dateForm.format(date))
                    Toast.makeText(applicationContext, dateForm.format(date), Toast.LENGTH_LONG).show()

                }
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase", "Getting request cancelled ${p0.message}")
                }

            })
        return START_NOT_STICKY
    }

}