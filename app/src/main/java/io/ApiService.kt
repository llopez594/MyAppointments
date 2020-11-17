package io

import Models.*
import io.response.LoginResponse
import Utils.Variables
import io.reactivex.Observable
import io.response.SimpleResponse
import okhttp3.ResponseBody
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

    @GET("user")
    fun getUser(@Header("Authorization") authHeader: String): Observable<User>

    @POST("user")
    fun postUser(@Header("Authorization") authHeader: String,
                 @Query("name") name: String,
                 @Query("phone") phone: String,
                 @Query("address") address: String
    ): Observable<ResponseBody>

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

    @GET("appointments")
    fun getAppointments(@Header("Authorization") authHeader: String): Observable<ArrayList<Appointment>>

    //    @Headers("Accept: application/json")
    @POST("appointments")
    fun storeAppointment(@Header("Authorization") authHeader: String,
                         @Query("description") description: String,
                         @Query("specialty_id") specialtyId: Int,
                         @Query("doctor_id") doctorId: Int,
                         @Query("schedule_date") scheduledDate: String,
                         @Query("scheduled_time") scheduledTime: String,
                         @Query("type") type: String
    ): Observable<SimpleResponse>

    @POST("register")
    fun postRegister(@Query("name") name: String,
                     @Query("email") email: String,
                     @Query("password") password: String,
                     @Query("password_confirmation") passwordConfirmation: String
    ): Observable<LoginResponse>

    @POST("fcm/token")
    fun postToken(@Header("Authorization") authHeader: String,
                  @Query("device_token") fcmToken: String): Observable<ResponseBody>
}