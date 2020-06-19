package com.example.findingmyphone

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class MyLocation : LocationListener {
    var myLocation : Location? = null
    constructor() : super() {
        myLocation = Location("me")
        myLocation!!.latitude = 0.0
        myLocation!!.longitude = 0.0

    }
    override fun onLocationChanged(location: Location?) {
        myLocation = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

}