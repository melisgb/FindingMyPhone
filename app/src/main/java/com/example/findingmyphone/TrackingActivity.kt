package com.example.findingmyphone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_tracking.*
import kotlinx.android.synthetic.main.contact_element.view.*

class TrackingActivity : AppCompatActivity() {
    var contactsList = ArrayList<Contact>()
    var contactAdapter : ContactAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        generateDummyData()

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
                //TODO: go to another activity
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

    inner class ContactAdapter : BaseAdapter {
        var listOfContacts = ArrayList<Contact>()
        var context : Context? = null

        constructor(context : Context, listOfContacts : ArrayList<Contact>) {
            this.context = context
            this.listOfContacts = listOfContacts
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val contact = listOfContacts[position]
            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactElemView = inflater.inflate(R.layout.contact_element, null)

            contactElemView.txtView_contactName.text = contact.name
            contactElemView.txtView_contactPhone.text = contact.phone

            return contactElemView
        }

        override fun getItem(position: Int): Any {
            return listOfContacts[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfContacts.size
        }

    }
}
