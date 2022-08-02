package hu.unideb.inf.ordertracker_android.network

import androidx.annotation.NonNull
import hu.unideb.inf.ordertracker_android.event.AuthFailedEvent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import okhttp3.logging.HttpLoggingInterceptor

import android.R.string.no




internal class UnauthorizedInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        if (response.code == 401) {
            EventBus.getDefault().post(AuthFailedEvent())
        }
        return response
    }
}

private val BASE_URL =
    "http://192.168.1.44:8080/"

object BackendApi {

    lateinit var retrofitService: BackendApiInterface

    var token: String = ""

    fun initRetrofitService() {

        val tokenInterceptor = Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Authorization", token).build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

        var client = OkHttpClient.Builder()
            .addInterceptor(UnauthorizedInterceptor())
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()


        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()

        retrofitService = retrofit.create(BackendApiInterface::class.java)
    }

}