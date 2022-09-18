package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.CommunityRemoteSource
import ajou.paran.entrip.repository.network.dto.community.RequestPost
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import javax.inject.Inject

class CommunityRepositoryImpl
@Inject
constructor(
    private val communityRemoteSource: CommunityRemoteSource
) : CommunityRepository {

    override suspend fun savePhoto(
        priority: Long,
        image: MultipartBody.Part,
        attempt: Int
    ): Long
    = when (val result = communityRemoteSource.savePhoto(priority, image)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3) -1 else savePhoto(priority, image, attempt + 1)
        }
    }


    override suspend fun findByIdPhoto(
        photoId: Long,
        attempt: Int
    ): ResponseFindByIdPhoto
    = when (val result = communityRemoteSource.findByIdPhoto(photoId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3)
                ResponseFindByIdPhoto(
                    photoId = -1,
                    photoUrl = "",
                    fileName = "",
                    priority = -1
                )
            else
                findByIdPhoto(photoId, attempt + 1)
        }
    }

    override suspend fun deletePhoto(photoId: Long): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun addPostsToPhotos(
        photoId: Long,
        postId: Long
    ): BaseResult<Boolean, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun savePost(requestPost: RequestPost, attempt: Int): Long
    = when(val result = communityRemoteSource.savePost(requestPost)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3)
                -1
            else
                savePost(requestPost, attempt + 1)
        }
    }


    override suspend fun findByIdPost(postId: Long, attempt: Int): ResponsePost
    = when(val result = communityRemoteSource.findByIdPost(postId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3)
                ResponsePost(
                    post_id = -1L,
                    title = "",
                    content = "",
                    author = "",
                    likeNumber = 0,
                    commentsNumber = 0,
                    photoList = listOf()
                )
            else
                findByIdPost(postId, attempt + 1)
        }
    }

    override suspend fun deletePost(postId: Long): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsListWithPageNum(pageNum: Long): List<ResponsePost>
    = when (val boardList = communityRemoteSource.getPostsListWithPageNum(pageNum)) {
        is BaseResult.Success -> {
            boardList.data
        }
        is BaseResult.Error -> {
            listOf()
        }
    }


    override suspend fun raiseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

}