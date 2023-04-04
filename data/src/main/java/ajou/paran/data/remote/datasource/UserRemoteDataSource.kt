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
        user_id : String,
        token : String
    )

    suspend fun searchUser(
        user_id_or_nickname : String
    )

    suspend fun findAllUsersByPlannerId(
        planner_id : Long
    )

    suspend fun addPlanners(
        planner_id : Long,
        user_id : String
    )

    suspend fun findAllPlannersByUser(
        user_id: String
    )

    suspend fun findUserById(
        user_id : String
    )

    suspend fun isExistUserInPlannerByUserId(
        planner_id : Long,
        user_id : String
    )

    suspend fun isExistUserInPlannerByNickname(
        planner_id : Long,
        nickname : String
    )

}