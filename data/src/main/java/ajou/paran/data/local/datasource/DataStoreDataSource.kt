package ajou.paran.data.local.datasource

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    fun fetchIdToken(): Flow<String>
    fun fetchAccessToken(): Flow<String>
    fun fetchRefreshToken(): Flow<String>
    fun fetchUserEmail(): Flow<String>
    fun fetchIsEntry(): Flow<Boolean>
    suspend fun saveIdToken(token: String)
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveUserEmail(email: String)
    suspend fun saveIsEntry(isEntry: Boolean)
    suspend fun clearIdToken()
    suspend fun clearAccessToken()
    suspend fun clearRefreshToken()
    suspend fun clearUserEmail()
    suspend fun clearIsEntry()
}
