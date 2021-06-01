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

class TrafficCameraAdapter(private val mCamera: MutableList<Camera>) : RecyclerView.Adapter<TrafficCameraAdapter.TrafficCameraViewHolder>() {

    //view holder: initializes view without binding data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrafficCameraViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.traffic_camera_row, parent, false)
        return TrafficCameraViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrafficCameraViewHolder, position: Int) {
        //val cam = mCamera[position].Features[position].Cameras[0]


        holder.camDesc.text = mCamera[position].Description

        val imgUrl = IMAGE_BASE_URL + mCamera[position].ImageUrl
        Picasso.get()
            .load(imgUrl)
            .into(holder.camImg)
        }
    //private val cameras: List<CameraInfo> = mutableListOf()
    class TrafficCameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val camImg: ImageView = itemView.findViewById(R.id.cam_img)
        val camDesc: TextView = itemView.findViewById(R.id.cam_desc)
    }
    override fun getItemCount() : Int {
        return mCamera.size
    }

    }
