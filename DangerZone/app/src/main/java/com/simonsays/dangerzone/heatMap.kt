package com.simonsays.dangerzone

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class heatMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var dangerPoints: List<LatLng>
    private var locationManager : LocationManager? = null
    var lat = 47.6614244
    var lon = -122.2683743

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heat_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        DataProvider.getData { result ->
            val Crimes = result.getJSONArray("crimes")
            addHeatMap(Crimes)
        }
        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lon = location.longitude
            lat = location.latitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
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
        // Add a marker in Sydney and move the camera
        //lat=47.6614244&lon=-122.2683743
        val uw = LatLng(47.6553, -122.3035)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(uw, 14f)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uw))
        mMap.uiSettings.isZoomGesturesEnabled = false
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        } else {
            mMap.isMyLocationEnabled = true
        }
    }

    fun addHeatMap(Crimes: JSONArray) {
        lateinit var list: List<LatLng>
        lateinit var listTitle: List<String>
        lateinit var listTime: List<String>

        try {
            list = readItems(Crimes) //change parameter if we want
            listTitle = readTitles(Crimes)
            listTime = readTimes(Crimes)
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show()
        }

        dangerPoints = list
/*
        for (i in 0..list.size- 1) {
            mMap.addMarker(MarkerOptions().position(list.get(i)).title(listTitle.get(i)))
        }
*/

        val mProvider = HeatmapTileProvider.Builder().radius(50)
            .data(list)
            .build()

        // if you ever need the mOverlay then please set this variable
        // equal to this line
        mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    fun readItems(array: JSONArray): ArrayList<LatLng> {
        val list = ArrayList<LatLng>()
        for (i in 0..array.length() - 1) {
            val coord = array.getJSONObject(i)
            val lat = coord.getDouble("lat")
            val lng = coord.getDouble("lon")
            list.add(LatLng(lat, lng))
        }
        return list
    }

    fun readTitles(array: JSONArray): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..array.length() - 1) {
            val crime = array.getJSONObject(i)
            val title = crime.getString("type")
            list.add(title)
        }
        return list
    }

    fun readTimes(array: JSONArray): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..array.length() - 1) {
            val time = array.getJSONObject(i)
            val timeStamp = time.getString("date").substring(9)
            list.add(timeStamp)
        }
        return list
    }


    fun checkIfNearCrime(resource: Int) {
        for (point in dangerPoints) {
            val pointLat = point.latitude
            val pointLon = point.longitude

            val distance = Math.hypot(Math.abs(lat-pointLat), Math.abs(lon - pointLon))
            val miles = distance
 }
        }
    }


//    // create a method to make a push notification (vibration)
//    fun pushNotification(): {
//
//    }


