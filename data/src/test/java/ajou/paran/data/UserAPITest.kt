package ajou.paran.data

import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserAPITest {

    private val userAPI = Retrofit.Builder()
        .baseUrl("https://2ntrip.link")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserAPI::class.java)


    @Test
    fun testSaveUserAccount() = runBlocking {
        val request = SaveUserAccountRequest(
            userId = "test@test.com",
            nickname = "test",
            photoUrl = "",
            token = "",
            gender = 0,
            password = "test1234",
        )
        val response = userAPI.saveUserAccount(request)
        // add assertions to check the response

        println(response)
    }

}