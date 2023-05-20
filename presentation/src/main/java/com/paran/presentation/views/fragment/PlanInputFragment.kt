package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlanner
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.common.route.HomeRoute
import com.paran.presentation.databinding.FragmentPlanInputBinding
import com.paran.presentation.views.activity.HomeActivity
import com.paran.presentation.views.viewmodel.PlanInputFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class PlanInputFragment : BaseETFragment<FragmentPlanInputBinding>(R.layout.fragment_plan_input) {
    companion object {
        fun newInstance(
            plannerId: Long,
            selectedDate: String
        ): PlanInputFragment = PlanInputFragment().apply {
            arguments = Bundle().apply {
                putLong("plannerId", plannerId)
                putString("selectedDate", selectedDate)
            }
        }

        private const val TAG = "PlanInputFrag"
    }

    private val viewModel: PlanInputFragmentViewModel by viewModels()

    override fun init() {
        binding.fragment = this
        binding.viewModel = viewModel

        kotlin.runCatching {
            viewModel.postDate(
                plannerId = requireArguments().getLong("plannerId", -1),
                selectedDate = requireArguments().getString("selectedDate", ""),
            )
        }.onFailure {
            (requireActivity() as HomeActivity).pushRoute(HomeRoute.Home.tag)
        }
    }

    fun modifyTime() {
        val currentTime = Calendar.getInstance()
        val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentTime.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { view, hour, minute ->
                val convertHour = when (hour < 10) {
                    true -> "0$hour"
                    false -> hour
                }
                val convertMinute = when (minute < 10) {
                    true -> "0$minute"
                    false -> minute
                }

                viewModel.inputTime.value = "$convertHour:$convertMinute"
            },
            startHour,
            startMinute,
            true
        ).show()
    }

    fun hideKeyboard(view: View, event: MotionEvent): Boolean {
        val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            getView()?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        return false
    }

    fun onClickBack() {
        when (val activity = requireActivity()) {
            is HomeActivity -> {
                activity.pushRoute(HomeRoute.PlannerDetail.tag)
            }
        }
    }

    fun onClickCheck() {
        kotlin.runCatching {
            viewModel.checkInputData()
        }.onFailure { exception -> Log.d(TAG, exception.toString()) }
    }

}