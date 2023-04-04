package ajou.paran.data.local.datasource

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    fun fetchIdToken(): Flow<String>
    fun fetchAccessToken(): Flow<String>
    fun fetchRefreshToken(): Flow<String>

    suspend fun saveIdToken(token: String)
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun clearIdToken()
    suspend fun clearAccessToken()
    suspend fun clearRefreshToken()
}
