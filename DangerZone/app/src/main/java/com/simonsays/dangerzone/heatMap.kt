package com.simonsays.dangerzone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class heatMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heat_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        DataProvider.getData { result ->
            return@getData
        }
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
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun addHeatMap() {
        var list: List<LatLng>? = null
        try {
            //list = readItems(R.raw.crimes) //change parameter if we want
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show()
        }
        val mProvider = HeatmapTileProvider.Builder()
            .data(list)
            .build()
        // if you ever need the mOverlay then please set this variable
        // equal to this line
        mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    fun readItems(resource: Int): ArrayList<LatLng> {
        val list = ArrayList<LatLng>()
        val inputStream  = getResources().openRawResource(resource)
        val json = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0..array.length() - 1) {
            val coord = array.getJSONObject(i)
            val lat = coord.getDouble("lat")
            val lng = coord.getDouble("lon")
            list.add(LatLng(lat, lng))
        }
        return list
    }


   func checkIfNearCrime(resource: Int): boolean {
        ArrayList<LatLng> crimes = readItems()
    }

}
