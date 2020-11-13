package IO

import Utils.LogUtil
import Utils.Variables
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpClient {
    private const val TAG: String = "Factory"

    private val interceptor = HttpLoggingInterceptor { message ->
        LogUtil.errorLog(TAG, "HttpLoggingInterceptor: $message")
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectionSpecs(
            listOf(
                ConnectionSpec.CLEARTEXT,
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS
            )
        )
        .connectTimeout(Variables.time_connect_out, TimeUnit.SECONDS)
        .writeTimeout(Variables.time_write_out, TimeUnit.SECONDS)
        .readTimeout(Variables.time_read_out, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(interceptor)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().addHeader("Accept", "application/json").build()
            )
        }
        /*.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                return chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + Variables.token!!)
                        .build())
            }
        })*/
        .build()
}