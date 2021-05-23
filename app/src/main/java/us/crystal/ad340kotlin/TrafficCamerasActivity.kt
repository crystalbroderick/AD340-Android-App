package us.crystal.ad340kotlin


import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_traffic_cameras.*

import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import us.crystal.ad340kotlin.network.Api

const val BASE_URL = "https://web6.seattle.gov/Travelers/api/Map/"

class TrafficCamerasActivity : AppCompatActivity() {
    private val TAG = "TrafficActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_cameras)
        checkNetworkState()
    }



    // Manage Network State: check the state of network connectivity with ConnectivityManager and NetworkInfo classes
    //NetworkInfo: describes status of network interface (Mobile or Wifi)
    private fun checkNetworkState() {
        // Answers queries about the state of network connectivity. Notify app when network change.
       //val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if(isConnected==true){
            val trafficRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            onConnect(trafficRecyclerView)
            Toast.makeText(this, "Connected!!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Boo, no internet!", Toast.LENGTH_LONG).show()
        }
    }


    private fun onConnect(trafficRecyclerView: RecyclerView) {

        val api:Api=Retrofit.Builder()
            .baseUrl("https://web6.seattle.gov/Travelers/api/Map/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
            val response = api.getCameraData().awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    trafficRecyclerView.adapter = TrafficCameraAdapter(data)
                    trafficRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    recyclerView.setHasFixedSize(true)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Hmmm..", Toast.LENGTH_LONG).show()
            }
        }
        }

}}
