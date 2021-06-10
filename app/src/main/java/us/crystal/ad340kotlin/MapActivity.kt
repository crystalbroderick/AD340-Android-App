package us.crystal.ad340kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import us.crystal.ad340kotlin.model.Camera
import us.crystal.ad340kotlin.model.Feature
import java.util.ArrayList

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val REQUEST_LOCATION_PERMISSIONS = 0

    private var mMap: GoogleMap? = null
    private var mClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocations: MutableList<Camera> = ArrayList()
    // Member variable
    private var mZoomLevel = 15f
    private var url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

       // mMap?.setInfoWindowAdapter(MarkerAdapter(this))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        checkNetworkState()

        getLocationData(url)
        // Create location request
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 5000
        mLocationRequest!!.fastestInterval = 3000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Create location callback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult != null) {
                    for (location in locationResult.locations) {
                        updateMap(location)
                    }
                }
            }
        }
        mClient = LocationServices.getFusedLocationProviderClient(this)

}

    private fun getLocationData(url: String) {
        val queue= Volley.newRequestQueue(this)
        Log.d("Volley", "new request queue")

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val camData = response.getJSONArray("Features")
                val len = camData.length()
                for (i in 1 until len) {
                    val cameras = camData.getJSONObject(i).getJSONArray("Cameras")
                    //fetches [lat,lng] from url
                    val points = camData.getJSONObject(i).getJSONArray("PointCoordinate")
                    // Log.d(TAG, points.toString())
                    val camLatLng = doubleArrayOf(points.getDouble(0), points.getDouble(1))
                    val location = Camera (
                        cameras.getJSONObject(0).getString("Id"),
                        cameras.getJSONObject(0).getString("Description"),
                        cameras.getJSONObject(0).getString("ImageUrl"),
                        cameras.getJSONObject(0).getString("Type"),
                        camLatLng,
                    )



                    mLocations.add(location)
                }
                showCamLocationMarker()
            }) { error -> Log.d(TAG, "Volley Error" + error.message) }
        queue.add(req)
       // Log.e(TAG, queue.toString())
    }

    private fun showCamLocationMarker() {
        // Log.e(TAG, points.toString())
        val len = mLocations.size
        for (i in 1 until len) {

            //Camera Location list
            val cL = mLocations[i]

            //Get Camera Position
            val position = LatLng(cL.points[0], cL.points[1])

            // Place a marker at the points location
            val marker = MarkerOptions()
                .title(cL.Description)
                .position(position)

     /*       mLocations.forEach { cL ->
                val marker = mMap?.addMarker(
                    MarkerOptions()
                        .title(cL.Description)
                        .position(position)
                )
                if (marker != null) {
                    marker.tag = cL
                }            marker.tag = mLocations*/

            // Add new marker
            mMap!!.addMarker(marker)
            //Log.e(TAG, marker.toString())
            }


    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        // Save zoom level
        mMap!!.setOnCameraMoveListener {
            val cameraPosition = mMap!!.cameraPosition
            mZoomLevel = cameraPosition.zoom
        }

        // Handle marker click
        mMap!!.setOnMarkerClickListener { marker ->
            Toast.makeText(
                this@MapActivity, """Lat: ${marker.position.latitude} Long: ${marker.position.longitude}""".trimIndent(), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun updateMap(location: Location) {
        // Get current location
        val myLatLng = LatLng(location.latitude, location.longitude)

        // Place a marker at the current location
        val myMarker = MarkerOptions()
            .title("You are here :D!")
            .position(myLatLng)

        // Add new marker
        mMap!!.addMarker(myMarker)

        // Move and zoom to current location at the street level
        val update = CameraUpdateFactory.newLatLngZoom(myLatLng, 15f)

        // Zoom to previously saved level
        // CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLatLng, mZoomLevela);
        mMap!!.animateCamera(update)
    }

    override fun onPause() {
        super.onPause()
        mClient!!.removeLocationUpdates(mLocationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            mClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    private fun hasLocationPermission(): Boolean {

        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSIONS)
            return false
        }
        return true
    }

    // Manage Network State: check the state of network connectivity with ConnectivityManager and NetworkInfo classes
    // NetworkInfo: describes status of network interface (Mobile or Wifi)
    private fun checkNetworkState() {
        // Answers queries about the state of network connectivity. Notify app when network change.
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      //  val loading = findViewById<ProgressBar>(R.id.error)
        if (isConnected == true) {
        //    loading.visibility = View.GONE
            Toast.makeText(this, "Connected!!", Toast.LENGTH_LONG).show()
        } else {
        //    loading.visibility = View.VISIBLE
            Toast.makeText(this, "Boo, no internet!", Toast.LENGTH_LONG).show()
        }

}}