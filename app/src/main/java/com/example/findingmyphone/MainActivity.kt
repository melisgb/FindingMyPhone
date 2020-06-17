package com.example.findingmyphone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permission
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var contactsList = ArrayList<Contact>()
    var contactAdapt : ContactAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.getPhoneNumber()

        contactsList.add(Contact("Melissa", "07383561085"))
        contactsList.add(Contact("Carlos", "07393901463"))
        contactsList.add(Contact("Lovie", "07393901420"))


        contactAdapt = ContactAdapter(this, contactsList)
        listView_trackers.adapter = contactAdapt

        listView_trackers.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val contactInfo = contactsList[position]
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
