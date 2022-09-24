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
import android.view.View
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
    private lateinit var boardCommentAdapter: BoardCommentAdapter

    override fun init(savedInstanceState: Bundle?) {
        subscribeObservers()
        setUpPost()
        setUpRecyclerView()
        setOnClickListener()
    }

    private fun setUpPost() {
        postId = intent.getLongExtra("postId", -1L)
        if (postId == -1L) {
            viewEscapeDialog("오류", "네트워크 상태를 확인해주세요.")
        }
        viewModel.loadPostData(postId)
    }

    private fun setUpRecyclerView() {
        boardImageAdapter = BoardImageAdapter()
        binding.boardImageList.run {
            adapter = boardImageAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RecyclerViewDecoration(30))
        }
        boardCommentAdapter = BoardCommentAdapter()
        binding.boardCommentList.run {
            adapter = boardCommentAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setOnClickListener() = binding.run {
        rangePostComment.setOnClickListener {
            when (etCommentContent.text.isNullOrEmpty()) {
                true -> {
                    // text is null or empty
                    viewDialog("필요", "텍스트는 최소 한 글자 이상 작성하셔야 합니다.")
                }
                false -> {
                    // text is exist
                    viewModel.postComment(postId, etCommentContent.text.toString())
                    etCommentContent.setText("")
                }
            }
        }
        boardBack.setOnClickListener { onBackPressed() }
    }

    private fun subscribeObservers() = viewModel.run {
        post.observe(this@BoardActivity) { post ->
            if (post.post_id != postId) {
                viewEscapeDialog("오류", "네트워크 오류가 발생하였습니다.")
            } else {
                Log.d(TAG, "postId: ${post.post_id}")
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
        commentList.observe(this@BoardActivity) {
            when (it) {
                null -> {
                    binding.run {
                        boardCommentList.visibility = View.GONE
                        commentProgress.visibility = View.VISIBLE
                    }
                }
                else -> {
                    binding.run {
                        boardCommentList.visibility = View.VISIBLE
                        commentProgress.visibility = View.GONE
                    }
                    boardCommentAdapter.setList(it)
                }
            }
        }
        postComment.observe(this@BoardActivity) {
            when (it) {
                -1L -> {
                    // post comment fail
                    viewDialog("오류", "네트워크 상태를 체크해주세요.")
                }
                else -> {
                    // post comment success & refresh data
                    viewModel.loadPostData(postId)
                }
            }
        }
    }

    private fun List<String>.toListResponseFindByIdPhoto(): List<ResponseFindByIdPhoto> {
        val mutableList = mutableListOf<ResponseFindByIdPhoto>()
        for (photoUrl in this) {
            mutableList.add(ResponseFindByIdPhoto(photoUrl = photoUrl))
        }
        return mutableList.toList()
    }

    private fun viewDialog(title: String, msg: String) = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        .show()

    private fun viewEscapeDialog(title: String, msg: String) = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("확인") { _, _ ->
            onBackPressed()
            finish()
        }
        .show()
}