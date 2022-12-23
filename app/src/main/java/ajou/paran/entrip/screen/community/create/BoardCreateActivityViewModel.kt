package ajou.paran.entrip.screen.community.create

import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
import ajou.paran.entrip.repository.network.dto.community.RequestPost
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.util.bitmapQualityResize
import ajou.paran.entrip.util.buildImageBodyPart
import ajou.paran.entrip.util.convertBitmapToFile
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import javax.inject.Inject

@HiltViewModel
class BoardCreateActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val communityRepositoryImpl: CommunityRepositoryImpl
) : ViewModel() {
    companion object {
        private const val TAG = "[BCActVM]"
    }

    private val _photoIdList: MutableLiveData<List<Long>> = MutableLiveData()
    val photoIdList: LiveData<List<Long>>
        get() = _photoIdList

    private val _photoList: MutableLiveData<List<ResponseFindByIdPhoto>> = MutableLiveData()
    val photoList: LiveData<List<ResponseFindByIdPhoto>>
        get() = _photoList

    private val _postId: MutableLiveData<Long> = MutableLiveData()
    val postId: LiveData<Long>
        get() = _postId

    private val photoMap: HashMap<Long, Long> = hashMapOf()

    val userId: String
        get() = sharedPreferences.getString("user_id", null) ?: ""

    fun postPhoto(
        uri: Uri,
        contentResolver: ContentResolver,
        context: Context,
        priority: Long = 1L
    ) = CoroutineScope(Dispatchers.IO).launch {
        photoMap[priority] = -1L
        _photoIdList.postValue(photoMap.values.toList())

        if (Build.VERSION.SDK_INT < 28) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val file = bitmap.convertBitmapToFile(getFileName(), context).bitmapQualityResize()

            withContext(Dispatchers.IO) {
                val photoId = communityRepositoryImpl.savePhoto(priority, file.buildImageBodyPart())
                photoMap[priority] = photoId
                _photoIdList.postValue(photoMap.values.toList())
                Log.d(TAG, "photoID: $photoId, Priority: $priority")
            }
        } else {
            val source = ImageDecoder.createSource(contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val file = bitmap.convertBitmapToFile(getFileName(), context).bitmapQualityResize()

            withContext(Dispatchers.IO) {
                val photoId = communityRepositoryImpl.savePhoto(priority, file.buildImageBodyPart())
                photoMap[priority] = photoId
                _photoIdList.postValue(photoMap.values.toList())
                Log.d(TAG, "photoID: $photoId, Priority: $priority")
            }
        }
    }

    fun postPost(title: String, context: String) = CoroutineScope(Dispatchers.IO).launch {
        val postId = communityRepositoryImpl.savePost(
            RequestPost(
                title = title,
                content = context,
                author = userId,
                photoIdList = photoMap.values.toList()
            )
        )
        _postId.postValue(postId)
    }

    fun findPhotos(list: List<Long>) = CoroutineScope(Dispatchers.IO).launch {
        val mutableList: MutableList<ResponseFindByIdPhoto> = mutableListOf()
        for (photoId in list.sorted()){
            val photo = communityRepositoryImpl.findByIdPhoto(photoId = photoId)
            mutableList.add(photo)
        }
        _photoList.postValue(mutableList.toList())
    }

    fun getFileName() = "${System.currentTimeMillis()}_${userId}_image.webp"
//    fun getFileName() = "${System.currentTimeMillis()}_image.png"

    fun mapClear() = photoMap.clear()
}