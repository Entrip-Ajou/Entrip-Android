package ajou.paran.entrip.screen.planner.mid



import ajou.paran.entrip.databinding.FragmentMidBinding
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MidFragment : Fragment(){

    companion object{
        private const val TAG = "[MidFragment]"
    }

    private val viewModel : MidViewModel by viewModels()
    private lateinit var binding : FragmentMidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMidBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.midViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = PlanAdapter()
        binding.rvPlan.adapter = categoryAdapter
        viewModel.items.observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
        }
    }
}