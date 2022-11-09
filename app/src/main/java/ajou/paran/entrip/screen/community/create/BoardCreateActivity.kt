package ajou.paran.entrip.screen.community.create

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityBoardcreateBinding
import ajou.paran.entrip.screen.community.BoardImageAdapter
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class BoardCreateActivity : BaseActivity<ActivityBoardcreateBinding>(R.layout.activity_boardcreate) {

    private val viewModel: BoardCreateActivityViewModel by viewModels()
    private val jobList: MutableList<Job> = mutableListOf()

    private lateinit var filterActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var boardImageAdapter: BoardImageAdapter

    override fun init(savedInstanceState: Bundle?) {
        binding.activity = this
        launchInit()
        setUpRecyclerView()
        subscribeObservers()
    }

    private fun launchInit() {
        filterActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data != null) {
                viewModel.mapClear()
                when (it.data!!.clipData != null) {
                    true -> {
                        val currentImage = it.data!!.clipData!!
                        val count = currentImage.itemCount
                        //5장 이상 선택한 경우
                        if (count > 5) {
                            Toast.makeText(applicationContext, "사진은 최대 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                            return@registerForActivityResult
                        }

                        //선택한 이미지의 갯수 만큼 반복
                        for (i in 0 until count) {
                            //선택한 이미지의 갯수만큼 이미지의 uri를 추출해서 리스트에 저장
                            jobList.add(
                                viewModel.postPhoto(
                                    uri = it.data?.clipData!!.getItemAt(i).uri,
                                    contentResolver = contentResolver,
                                    context = applicationContext,
                                    priority = (i + 1).toLong()
                                )
                            )
                        }
                    }
                    false -> {
                        // Single image
                        val currentImageUri = it.data?.data
                        try {
                            currentImageUri?.let {
                                jobList.add(
                                    viewModel.postPhoto(
                                        uri = currentImageUri,
                                        contentResolver = contentResolver,
                                        context = applicationContext
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ActivityResult","something wrong")
            }
        }
    }

    private fun setUpRecyclerView() {
        boardImageAdapter = BoardImageAdapter()
        binding.boardCreateImageList.run {
            adapter = boardImageAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RecyclerViewDecoration(30))
        }
    }

    private fun subscribeObservers() = viewModel.run {
        if (photoIdList.hasActiveObservers()) {
            photoIdList.removeObservers(this@BoardCreateActivity)
        }
        photoIdList.observe(this@BoardCreateActivity) {
            viewModel.findPhotos(it)
        }
        if (photoList.hasActiveObservers()) {
            photoList.removeObservers(this@BoardCreateActivity)
        }
        photoList.observe(this@BoardCreateActivity) {
            boardImageAdapter.setList(it)
        }
        if (postId.hasActiveObservers()) {
            postId.removeObservers(this@BoardCreateActivity)
        }
        postId.observe(this@BoardCreateActivity) {
            Log.d("Test", "Success Save Post $it")
            when {
                it > 0 -> {
                    Intent().run {
                        putExtra("postId", it)
                        setResult(RESULT_OK, this)
                    }
                }
                else -> {
                    Intent().run {
                        setResult(RESULT_CANCELED)
                    }
                }
            }

            finish()
        }
    }

    fun moveToAddImage(): Unit = when (this::filterActivityLauncher.isInitialized) {
        true -> {
            filterActivityLauncher.launch(
                Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
            )
        }
        false -> {  }
    }

    fun postBoard(): Any = when (binding.boardCreateTitle.text.isNotEmpty() && binding.boardCreateContent.text.isNotEmpty()) {
        true -> {
            when (jobList.isEmpty()) {
                true -> {
                    viewModel.postPost(
                        title = binding.boardCreateTitle.text.toString(),
                        context = binding.boardCreateContent.text.toString()
                    )
                }
                false -> {
                    var result = true
                    for (job in jobList) {
                        if (!job.isCompleted) {
                            result = false
                            break
                        }
                    }
                    if (result) {
                        viewModel.postPost(
                            title = binding.boardCreateTitle.text.toString(),
                            context = binding.boardCreateContent.text.toString()
                        )
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("오류")
                            .setMessage("이미지 업로드 중 입니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
        false -> {
            AlertDialog.Builder(this)
                .setTitle("오류")
                .setMessage("제목과 내용을 다시 확인해주세요.")
                .setCancelable(false)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}