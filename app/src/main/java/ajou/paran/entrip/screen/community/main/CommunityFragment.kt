package ajou.paran.entrip.screen.community.main

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentCommunityBinding
import ajou.paran.entrip.screen.community.board.BoardActivity
import ajou.paran.entrip.screen.community.create.BoardCreateActivity
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community){
    companion object{
        const val TAG = "[CommunityFragment]"
    }

    private val viewModel: CommunityFragmentViewModel by viewModels()

    private lateinit var createActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var rawBoardAdapter: RawBoardAdapter

    override fun init() {
        setActivityLauncher()
        setUpRecyclerView()
        setListener()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        rawBoardAdapter.clearList()
        viewModel.refreshPageData()
    }

    private fun setActivityLauncher() {
        createActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    Log.d(TAG, "postId: ${it.data!!.getLongExtra("postId", -1L)}")
                    startActivity(
                        Intent(activity, BoardActivity::class.java).apply {
                            putExtra("postId", it.data!!.getLongExtra("postId", -1L))
                        }
                    )
                }
                else -> {
                    Toast.makeText(context, "게시글 생성 취소", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        rawBoardAdapter = RawBoardAdapter()
        binding.communityRecyclerView.run {
            adapter = rawBoardAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setListener() {
        binding.run {
            communityScrollView.setOnScrollChangeListener {
                    view: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                when (scrollY) {
                    view.getChildAt(0).measuredHeight - view.measuredHeight -> {
                        communityProgress.visibility = View.VISIBLE
                        viewModel.getNextPage()
                    }
                }
            }
            communityBtnCreate.setOnClickListener {
                createActivityLauncher.launch(Intent(activity, BoardCreateActivity::class.java))
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.boardItemList.observe(this) {
            rawBoardAdapter.setList(it)
            binding.communityProgress.visibility = View.INVISIBLE
        }
    }

}