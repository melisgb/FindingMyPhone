package com.example.findingmyphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_tracking.*
import kotlinx.android.synthetic.main.contact_element.view.*

class TrackingActivity : AppCompatActivity() {
    var contactsList = ArrayList<Contact>()
    var contactAdapter : ContactAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        //generateDummyData()
        checkPermission()

        contactAdapter = ContactAdapter(this, contactsList)
        listView_contacts.adapter = contactAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tracking_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.it_addContact -> {
                checkPermission()
            }
            R.id.it_finishActivity -> {
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    fun generateDummyData(){
        contactsList.add(Contact("Melissa", "07383561085"))
        contactsList.add(Contact("Carlos", "07393901463"))
        contactsList.add(Contact("Lovie", "07393901420"))
    }

    //to get Contacts App info
    val PERMISION_READ_CODE = 123
    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISION_READ_CODE
            )
            return
        }
        chooseFromContacts()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISION_READ_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseFromContacts()
                }
                else{
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    val PICKED_IMG_CODE = 1111
    fun chooseFromContacts() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICKED_IMG_CODE)
    }
    //after the user pickes an image.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            PICKED_IMG_CODE -> {
                if(resultCode == Activity.RESULT_OK){
                    val contactData = data!!.data
                    val cursor = contentResolver.query(contactData!!, null, null, null,null) //to get data from the contentProvider

                    if(cursor!!.moveToFirst()){
                        val contactId = cursor.getString(cursor.getColumnIndexOrThrow(
                            ContactsContract.Contacts._ID))
                        val hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if(hasPhone.equals("1")){
                            val phonesCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null)

                            phonesCursor!!.moveToFirst()
                            val phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val name = phonesCursor.getString(phonesCursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME))
                            contactsList.add(Contact(name, phoneNumber))
                            contactAdapter!!.notifyDataSetChanged()
                        }
                    }

                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
