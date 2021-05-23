package us.crystal.ad340kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import us.crystal.ad340kotlin.model.*

const val IMAGE_BASE_URL = "https://www.seattle.gov/trafficcams/images/"
const val IMAGE_BASE_URL2 = "https://images.wsdot.wa.gov/nw/"

class TrafficCameraAdapter(private val cameraList: Camera) : RecyclerView.Adapter<TrafficCameraAdapter.TrafficCameraViewHolder>() {


    //view holder: initializes view without binding data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrafficCameraViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.traffic_camera_row, parent, false)
        return TrafficCameraViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrafficCameraViewHolder, position: Int) {
        //val currentCamera = cameraList.Features[position].Camera
        holder.location.text = cameraList.Features[position].Camera[0].Description
        val imageUrl = IMAGE_BASE_URL + cameraList.Features[position].Camera[0].ImageUrl

        Picasso.get()
            .load(imageUrl)
            .into(holder.streetView)
    }

    override fun getItemCount() : Int {
        return cameraList.Features.size
    }

    inner class TrafficCameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.location)
        val streetView: ImageView = itemView.findViewById(R.id.streetView)
    }

}