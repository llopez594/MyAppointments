package IO

import IO.response.LoginResponse
import Models.Doctor
import Models.Schedule
import Models.Specialty
import Utils.Variables
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import kotlin.collections.ArrayList

interface ApiService {
    companion object Factory {
        private val BASE_URL = Variables.direction()

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClient.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("specialties")
    fun getSpecialties(): Observable<ArrayList<Specialty>>

    @GET( "specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialty: Int): Observable<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId: Int,
                 @Query("date") date: String): Observable<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email: String,
                 @Query("password") password: String): Observable<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Observable<ResponseBody>


}