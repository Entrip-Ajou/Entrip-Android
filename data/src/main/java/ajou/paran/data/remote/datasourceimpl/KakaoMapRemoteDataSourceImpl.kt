package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.KakaoMapAPI
import ajou.paran.data.remote.datasource.KakaoMapRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class KakaoMapRemoteDataSourceImpl
@Inject
constructor(
    private val kakaoMapAPI: KakaoMapAPI,
) : KakaoMapRemoteDataSource {

    override suspend fun getSearchKeyword(
        query: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}