package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.ResultSearchKeyword
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApi {
    companion object{
        const val Kakao_URL = "https://dapi.kakao.com/"
    }

    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Query("query") query : String
    ) : Response<ResultSearchKeyword>
}