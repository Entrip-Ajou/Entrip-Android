package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.response.GetSearchKeywordResponseList
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoMapAPI {

    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Query("query") query: String
    ): GetSearchKeywordResponseList

}