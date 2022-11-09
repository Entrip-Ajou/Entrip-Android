package ajou.paran.entrip.util

import ajou.paran.entrip.model.Comment
import ajou.paran.entrip.model.Media
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.viewpager2.widget.ViewPager2
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

fun AppCompatActivity.share(media: Media, title: String = "Share with...") {
    val share = Intent(Intent.ACTION_SEND)
    share.type = "image/*"
    share.putExtra(Intent.EXTRA_STREAM, media.uri)
    startActivity(Intent.createChooser(share, title))
}

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun View.onWindowInsets(action: (View, WindowInsetsCompat) -> Unit) {
    ViewCompat.requestApplyInsets(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        action(v, insets)
        insets
    }
}

var View.topMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin
    set(value) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = value }
    }

var View.bottomMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    set(value) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> { bottomMargin = value }
    }

fun ViewPager2.onPageSelected(action: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            action(position)
        }
    })
}

fun Window.fitSystemWindows() {
    WindowCompat.setDecorFitsSystemWindows(this, false)
}

fun File.buildImageBodyPart(): MultipartBody.Part
= MultipartBody.Part.createFormData("photos", this.name, this.asRequestBody("image/*".toMediaTypeOrNull()))

fun Bitmap.convertBitmapToFile(fileName: String, context: Context): File {
    //create a file to write bitmap data
    val file = File(context.cacheDir, fileName)
    file.createNewFile()

    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitMapData = bos.toByteArray()

    //write the bytes in file
    var fos: FileOutputStream? = null

    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return file
}

fun File.bitmapQualityResize(): File
= try {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    options.inSampleSize = 6

    var fis = FileInputStream(this)
    BitmapFactory.decodeStream(fis, null, options)
    fis.close()

    val REQUSETED_SIZE = 75

    var scale = 1
    while (
        options.outWidth / scale / 2 >= REQUSETED_SIZE
        && options.outHeight / scale / 2 >= REQUSETED_SIZE
    ) {
        scale *= 2
    }

    val secondOptions = BitmapFactory.Options()
    secondOptions.inSampleSize = scale
    fis = FileInputStream(this)

    val selectedBitmap = BitmapFactory.decodeStream(fis, null, secondOptions)
    fis.close()

    this.createNewFile()
    val fos = FileOutputStream(this)

    selectedBitmap?.compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, fos)
    fos.close()

    this
} catch (e: Exception) {
    this
}

fun List<ResponseComment>.toCommentList(): List<Comment> {
    val mutableList = mutableListOf<Comment>()
    this.forEach {
        mutableList.add(Comment(it))
    }
    return mutableList.toList()
}