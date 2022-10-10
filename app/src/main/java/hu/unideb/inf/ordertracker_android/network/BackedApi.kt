package hu.unideb.inf.ordertracker_android.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import hu.unideb.inf.ordertracker_android.event.AuthFailedEvent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime

private const val BASE_URL =
    "http://192.168.1.44:8080/"

object BackendApi {

    lateinit var retrofitService: BackendApiInterface

    var token: String = ""

    fun initRetrofitService() {

        val unauthorizedInterceptor = object: Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                if (response.code == 401) {
                    EventBus.getDefault().post(AuthFailedEvent())
                }
                return response
            }
        }

        val tokenInterceptor = Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Authorization", token).build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = GsonBuilder().registerTypeAdapter(
            LocalDateTime::class.java,
            object : JsonDeserializer<LocalDateTime> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): LocalDateTime {
                    return LocalDateTime.parse(json?.asJsonPrimitive?.asString)
                }

            }).create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .baseUrl(BASE_URL)
            .build()

        retrofitService = retrofit.create(BackendApiInterface::class.java)
    }

}
