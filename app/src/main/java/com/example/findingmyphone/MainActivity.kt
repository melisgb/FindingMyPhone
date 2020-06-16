package com.example.findingmyphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.getPhoneNumber()



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.it_addTracker ->{
                val intent = Intent(this, TrackingActivity::class.java)
                startActivity(intent)
            }
            R.id.it_help ->{
                //TODO : ask friends for help
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}
