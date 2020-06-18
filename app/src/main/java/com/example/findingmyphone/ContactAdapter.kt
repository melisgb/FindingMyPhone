package com.example.findingmyphone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.contact_element.view.*

class ContactAdapter : BaseAdapter {
    var listOfContacts = ArrayList<Contact>()
    var context : Context? = null

    constructor(context : Context, listOfContacts : ArrayList<Contact>) {
        this.context = context
        this.listOfContacts = listOfContacts
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contact = listOfContacts[position]

        if(contact.name=="No value") {
            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactElemView = inflater.inflate(R.layout.contact_none_element, null)

            contactElemView.txtView_contactName.text = contact.name

            return contactElemView
        }
        else {
            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactElemView = inflater.inflate(R.layout.contact_element, null)

            contactElemView.txtView_contactName.text = contact.name
            contactElemView.txtView_contactPhone.text = contact.phone

            return contactElemView
        }
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