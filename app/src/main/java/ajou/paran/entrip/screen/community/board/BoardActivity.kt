package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityBoardBinding
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.screen.community.BoardImageAdapter
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardActivity : BaseActivity<ActivityBoardBinding>(R.layout.activity_board) {
    companion object {
        private const val TAG = "[BoardActivity]"
    }

    private val viewModel: BoardActivityViewModel by viewModels()

    private var postId: Long = -1L

    private lateinit var boardImageAdapter: BoardImageAdapter

    override fun init(savedInstanceState: Bundle?) {
        postId = intent.getLongExtra("postId", -1L)
        setUpRecyclerView()
        subscribeObservers()
        viewModel.loadPostData(postId)
    }

    private fun setUpRecyclerView() {
        boardImageAdapter = BoardImageAdapter()
        binding.boardImageList.run {
            adapter = boardImageAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RecyclerViewDecoration(30))
        }
    }

    private fun subscribeObservers() {
        viewModel.post.observe(this) { post ->
            if (post.post_id != postId) {
                AlertDialog.Builder(this)
                    .setTitle("오류")
                    .setMessage("네트워크 오류가 발생하였습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인") { _, _ -> finish() }
                    .show()
            } else {
                binding.run {
                    boardTitle.text = post.title
                    boardAuthor.text = post.author
                    boardContent.text = post.content
                    boardTvLikeNum.text = post.likeNumber.toString()
                    boardTvCommentNum.text = post.commentsNumber.toString()
                    boardImageAdapter.setList(post.photoList.toListResponseFindByIdPhoto())
                }
            }
        }
    }

    private fun List<String>.toListResponseFindByIdPhoto(): List<ResponseFindByIdPhoto> {
        val mutableList = mutableListOf<ResponseFindByIdPhoto>()
        for (photoUrl in this) {
            mutableList.add(
                ResponseFindByIdPhoto(
                    photoId = -1L,
                    photoUrl = photoUrl,
                    fileName = "",
                    priority = -1L
                )
            )
        }
        return mutableList.toList()
    }
}