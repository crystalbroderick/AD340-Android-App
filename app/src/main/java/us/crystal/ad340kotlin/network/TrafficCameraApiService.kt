package us.crystal.ad340kotlin.network



import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import us.crystal.ad340kotlin.model.Camera
import us.crystal.ad340kotlin.model.CameraInfo

private const val URL = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"

// API interface - specified endpoints.
interface ApiInterface {
    @GET("Data?zoomId=13&type=2")
    fun getCameraData(): Call<CameraInfo>

    //Service Builder and object
    companion object {

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://web6.seattle.gov/Travelers/api/Map/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}