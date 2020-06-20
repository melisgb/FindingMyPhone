package com.example.findingmyphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    var contactsList = ArrayList<Contact>()
    var contactAdapt : ContactAdapter? = null
    val mDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        val phoneNum = userData.getPhoneNumber()
        editText.setText(phoneNum)

        if(phoneNum.equals("empty")){
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this!!.startActivity(intent)
        }
        getTrackers()

        contactAdapt = ContactAdapter(this, contactsList)
        listView_trackers.adapter = contactAdapt

        listView_trackers.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val contactInfo = contactsList[position]

            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:MM:ss")
            val currDate = Date()
            mDatabase.child("Users").child(contactInfo.phone!!).child("login")
                .setValue(dateFormat.format(currDate).toString())

            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("phoneNumber", contactInfo.phone)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()

        val userData = UserData(this)
        if(userData.getPhoneNumber().isEmpty()){
            return
        }
        getTrackers()
        if(MyService.isServiceRunning) return
        checkContactsAppPermission()
        checkLocationPermission()
    }

    fun getTrackers(){
        val userData = UserData(this)

        mDatabase.child("Users").child(userData.getPhoneNumber()).child("trackers")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    try {
                        var dataSnap = dataSnapShot.getValue(object : GenericTypeIndicator<HashMap<String, Boolean>>() {})
                        contactsList.clear()

                        if (dataSnap == null ){
                            contactsList.add(Contact("No value", "NV"))
                            contactAdapt!!.notifyDataSetChanged()
                            return
                        }
                        else {
                            for (phoneKey in dataSnap.keys) {
                                val name = myContactsList[phoneKey]
                                    //when accesing dataSnap[phoneKey], will return always true.
                                contactsList.add(Contact(name.toString(), phoneKey))
                            }
                            contactAdapt!!.notifyDataSetChanged()
                        }
                    }catch (ex: Exception){
                        Log.e("Firebase Getting Tracks", ex.message, ex)
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Firebase", "Firebase Getting users cancelled ${p0.message}")
                }


            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.it_addTracker -> {
                val intent = Intent(this, TrackingActivity::class.java)
                startActivity(intent)
            }
            R.id.it_help -> {
                //TODO : ask friends for help
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    //to get Contacts App info
    val PERMISION_READ_CODE = 123
    fun checkContactsAppPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISION_READ_CODE
            )
            return
        }
        loadContacts()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISION_READ_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadContacts()
                }
                else{
                    Toast.makeText(applicationContext, "Permission contacts denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISION_LOCATION_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }
                else{
                    Toast.makeText(applicationContext, "Permission location denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    var myContactsList = HashMap<String, String>()
    fun loadContacts(){
        try{
            myContactsList.clear()

            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null)

            cursor!!.moveToFirst()
            do{
                val contactName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val contactPhone = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                myContactsList.put(UserData.formatPhoneNum(contactPhone), contactName)
            }while((cursor.moveToNext()))
        }catch (ex: Exception){
            Log.e("Error Loading Contacts", ex.message)
        }

    }


    //to get Contacts App info
    val PERMISION_LOCATION_CODE = 234
    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISION_LOCATION_CODE
            )
            return

        }
        getUserLocation()
    }


    fun getUserLocation(){
        //start service to run in background
        if(!MyService.isServiceRunning){
            val intent = Intent(baseContext, MyService::class.java)
//            startActivity(intent)
            startService(intent)
        }
    }




}
