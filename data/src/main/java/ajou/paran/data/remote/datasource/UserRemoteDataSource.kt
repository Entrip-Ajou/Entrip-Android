package ajou.paran.data.remote.datasource

interface UserRemoteDataSource {

    suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String
    )

    suspend fun signInByUserAccount(
        userId: String,
        password: String
    )

    suspend fun isExistUserByUserId(
        userId: String
    )

    suspend fun isExistUserByNickname(
        nickname: String
    )

    suspend fun saveUser(
        userId: String,
        gender: Int,
        nickname: String,
        photoUrl: String
    )

    suspend fun updateToken(
        userId : String,
        token : String
    )

    suspend fun searchUser(
        userIdOrNickname : String
    )

    suspend fun findAllUsersByPlannerId(
        plannerId : Long
    )

    suspend fun addPlanners(
        plannerId : Long,
        userId : String
    )

    suspend fun findAllPlannersByUser(
        userId: String
    )

    suspend fun findUserById(
        userId : String
    )

    suspend fun isExistUserInPlannerByUserId(
        plannerId : Long,
        userId : String
    )

    suspend fun isExistUserInPlannerByNickname(
        plannerId : Long,
        nickname : String
    )

}