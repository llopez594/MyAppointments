package IO

import Models.Doctor
import Models.Specialty
import Utils.LogUtil
import Utils.Variables
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
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

    @GET(value = "specialties")
    fun getSpecialties(): Observable<ArrayList<Specialty>>

    @GET(value = "specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialty: Int): Observable<ArrayList<Doctor>>

}