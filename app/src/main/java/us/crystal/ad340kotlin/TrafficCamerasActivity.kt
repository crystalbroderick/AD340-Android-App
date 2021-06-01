package us.crystal.ad340kotlin


import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_display_message.*
import us.crystal.ad340kotlin.model.Camera
import us.crystal.ad340kotlin.model.CameraXX


import java.util.ArrayList

const val TRAFFIC_CAM_API = "https://web6.seattle.gov/Travelers/api/Map/"
const val TAG = "Traffic Activity"
class TrafficCamerasActivity : AppCompatActivity() {

    private var mCamera: MutableList<Camera> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_cameras)
        checkNetworkState()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TrafficCameraAdapter(mCamera)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)


        val queue=Volley.newRequestQueue(this)
        var url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"

        val req = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val camData = response.getJSONArray("Features")
                val len = camData.length()
                for (i in 1 until len) {
                    val cameras = camData.getJSONObject(i).getJSONArray("Cameras")
                    val camera = Camera(
                        cameras.getJSONObject(0).getString("Id"),
                        cameras.getJSONObject(0).getString("Description"),
                        cameras.getJSONObject(0).getString("ImageUrl"),
                        cameras.getJSONObject(0).getString("Type"),
                    )
                    mCamera.add(camera)
                }
                adapter.notifyDataSetChanged()
            }) { error -> Log.d(TAG, "Volley Error" + error.message) }
        queue.add(req)
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
        val loading = findViewById<ProgressBar>(R.id.error)
        if (isConnected == true) {
            loading.visibility = View.GONE
            Toast.makeText(this, "Connected!!", Toast.LENGTH_LONG).show()
        } else {
            loading.visibility = View.VISIBLE
            Toast.makeText(this, "Boo, no internet!", Toast.LENGTH_LONG).show()
        }


    }
}