package ajou.paran.data.remote.datasource

interface NoticeRemoteDataSource {

    suspend fun saveNotice(
        author: String,
        title: String,
        content: String,
        plannerId: Long,
    )

    suspend fun updateNoticeById(
        noticeId: Long,
        title: String,
        content: String,
    )

    suspend fun deleteNoticeById(
        noticeId: Long,
    )

    suspend fun fetchAllNoticesByPlannerId(
        plannerId: Long,
    )

    suspend fun fetchNoticeById(
        noticeId: Long,
    )

}