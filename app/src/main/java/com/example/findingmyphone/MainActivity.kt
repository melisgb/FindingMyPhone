package com.example.findingmyphone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.security.Permission
import java.util.jar.Manifest

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
        }
    }

    override fun onResume() {
        super.onResume()
        getTrackers()
    }

    fun getTrackers(){
        val userData = UserData(this)
        if(userData.getPhoneNumber().isEmpty()){
            return
        }
        else {
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
                                    val name = dataSnap[phoneKey] as Boolean
                                    println("Name Tracker: $name")
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


}
