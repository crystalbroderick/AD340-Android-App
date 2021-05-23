package us.crystal.ad340kotlin.network

import retrofit2.Call
import retrofit2.http.GET
import us.crystal.ad340kotlin.model.Camera
import us.crystal.ad340kotlin.model.CameraInfo

interface Api {

        @GET("Data?zoomId=13&type=2")
        fun getCameraData(): Call<Camera>

    }