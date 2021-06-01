package us.crystal.ad340kotlin.network

import retrofit2.Call
import retrofit2.http.GET
import us.crystal.ad340kotlin.model.Camera

interface ApiRequest {
    @GET("Data?zoomId=13&type=2")
    fun getCameraData(): Call<Camera>

}