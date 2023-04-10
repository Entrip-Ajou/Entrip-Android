package ajou.paran.data

import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import ajou.paran.data.remote.model.response.SaveUserAccountResponse
import ajou.paran.data.utils.interceptors.AuthInterceptor
import ajou.paran.data.utils.interceptors.NetworkInterceptor
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserAPITest {

    private val userAPI = Retrofit.Builder()
        .client(
            OkHttpClient.Builder().apply {
                readTimeout(10, TimeUnit.SECONDS)
                connectTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
                addInterceptor(HttpLoggingInterceptor())
            }.build()
        )
        .baseUrl("https://2ntrip.link/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserAPI::class.java)


    @Test
    fun testSaveUserAccount() = runBlocking {
        val request = SaveUserAccountRequest(
            userId = "test1@test.com",
            nickname = "",
            photoUrl = "",
            token = "",
            gender = 0,
            password = "test1234",
        )
        var response: SaveUserAccountResponse? = null

        kotlin.runCatching {
            response = userAPI.saveUserAccount(request)
        }.onFailure { exception ->
            println(exception)
            response = null
        }
        // add assertions to check the response

        println(response)

        assert(response == SaveUserAccountResponse(
                userId = "test@test.com",
                nickname = "",
                photoUrl = "",
                token = "",
                gender = 0,
            )
        )
    }

}