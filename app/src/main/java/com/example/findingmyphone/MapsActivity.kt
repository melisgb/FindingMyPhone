package com.example.findingmyphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var mDatabaseRef : DatabaseReference? = null

    companion object{
        var contactLocation = LatLng(-0.0, 0.0)
        var lastLogin : String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val intentBundle = intent.extras
        val contactPhoneNum = intentBundle!!.getString("phoneNumber")
        mDatabaseRef = FirebaseDatabase.getInstance().reference

        mDatabaseRef!!.child("Users").child(contactPhoneNum!!).child("location")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnap: DataSnapshot) {
                    Log.d("Carlos printea", "")
                    try{
                        var snap = dataSnap.getValue(object : GenericTypeIndicator<HashMap<String, Any>>() {})

                        if(snap.isNullOrEmpty()) return

                        val lat = snap["lat"] as Double
                        val long = snap["long"] as Double
                        val lastSession = snap["lastSession"] as String
                        contactLocation = LatLng(lat, long)
                        lastLogin = lastSession
                        loadMap()

                    }catch (ex : Exception){
                        Log.e("Firebase Getting Tracks", ex.message, ex)
                    }


                }
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase", "Getting map cancelled ${p0.message}")
                }
            })
    }

    fun loadMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(contactLocation).title(lastLogin))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(contactLocation, 15f))
    }
}
