package ajou.paran.data.remote.datasource


interface KakaoMapRemoteDataSource {

    suspend fun getSearchKeyword(
        query: String
    )

}