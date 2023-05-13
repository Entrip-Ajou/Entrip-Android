package ajou.paran.data.local.datasource

import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {

    fun fetchIdToken(): Flow<String>
    fun fetchAccessToken(): Flow<String>
    fun fetchRefreshToken(): Flow<String>
    fun fetchUserEmail(): Flow<String>

    suspend fun saveIdToken(idToken: String)
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
//    suspend fun saveToken(userToken: UserToken)
    suspend fun saveUserEmail(email: String)

    suspend fun clearIdToken()
    suspend fun clearToken()
    suspend fun clearUserEmail()

}