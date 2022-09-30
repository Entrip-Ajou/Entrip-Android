package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityBoardBinding
import ajou.paran.entrip.model.Comment
import ajou.paran.entrip.model.CommentType
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.screen.community.BoardImageAdapter
import ajou.paran.entrip.util.toCommentList
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    private val imm: InputMethodManager
        get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    private var postId: Long = -1L
    private var commentId: Long = -1L

    private var commentType: CommentType = CommentType.Default
    private val commentWrite: Pair<CommentType, Long>
        get() = if (commentType == CommentType.Default) Pair(commentType, postId) else Pair(commentType, commentId)

    private lateinit var boardImageAdapter: BoardImageAdapter
    private lateinit var boardCommentAdapter: BoardCommentAdapter

    override fun init(savedInstanceState: Bundle?) {
        setUpRecyclerView()
        subscribeObservers()
        setUpPost()
        setOnClickListener()
    }

    private fun setUpPost() {
        postId = intent.getLongExtra("postId", -1L)
        if (postId == -1L) {
            viewEscapeDialog("네트워크 상태를 확인해주세요.")
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
        boardCommentAdapter.onItemClick = { defaultWriteSetting() }
        boardCommentAdapter.onChildItemClick = { defaultWriteSetting() }
        binding.boardCommentList.run {
            adapter = boardCommentAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setOnClickListener() = binding.run {
        boardParent.setOnClickListener { defaultWriteSetting() }
        boardImageList.setOnClickListener { defaultWriteSetting() }
        rangePostComment.setOnClickListener {
            when (etCommentContent.text.isNullOrEmpty()) {
                true -> {
                    // text is null or empty
                    viewDialog("필요", "텍스트는 최소 한 글자 이상 작성하셔야 합니다.")
                }
                false -> {
                    // text is exist
                    when (commentWrite.first) {
                        CommentType.Default -> {
                            viewModel.postComment(commentWrite.second, etCommentContent.text.toString())
                            etCommentContent.setText("")
                            defaultWriteSetting()
                        }
                        CommentType.Nested -> {
                            viewModel.postNestedComment(commentWrite.second, etCommentContent.text.toString())
                            etCommentContent.setText("")
                            defaultWriteSetting()
                        }
                    }
                }
            }
        }
        boardBack.setOnClickListener { onBackPressed() }
    }

    private fun subscribeObservers() = viewModel.run {
        post.observe(this@BoardActivity) { post ->
            if (post.post_id != postId) {
                viewEscapeDialog("네트워크 오류가 발생하였습니다.")
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
                    testCommentList.addAll(it.toCommentList())
                    loadNestedComment(testCommentList)
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
        postNestedComment.observe(this@BoardActivity) {
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
        isSuccessNestedComment.observe(this@BoardActivity) {
            if (it) {
                boardCommentAdapter.setList(testCommentList)
            }
        }
        boardCommentAdapter.commentLiveData.observe(this@BoardActivity) {
            when (it.comment.commentId) {
                -1L -> {
                    defaultWriteSetting()
                }
                else -> {
                    binding.etCommentContent.requestFocus()
                    imm.showSoftInput(binding.etCommentContent, 0)
                    commentType = CommentType.Nested
                    commentId = it.comment.commentId
                }
            }
        }
    }

    private fun defaultWriteSetting() {
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        commentType = CommentType.Default
        commentId = -1L
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

    private fun viewEscapeDialog(msg: String) = AlertDialog.Builder(this)
        .setTitle("오류")
        .setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("확인") { _, _ ->
            onBackPressed()
            finish()
        }
        .show()

}