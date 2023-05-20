package com.paran.presentation.common.widgets

import android.annotation.SuppressLint
import com.google.android.material.datepicker.RangeDateSelector


@SuppressLint("RestrictedApi")
class CustomMaterialDatePicker: RangeDateSelector() {
    companion object{
        const val TAG = "[CustomMaterialDatePicker]"
    }

    private var first = true
    private var firstDate: Long? = null

    override fun select(selection: Long) {
        when (first || selection < firstDate!!) {
            true -> {
                super.select(selection)
                super.select(selection)
                firstDate = selection
                first = false
            }
            false -> {
                super.select(firstDate!!)
                super.select(selection)
                if (selection != firstDate)
                    first = true
            }
        }
    }

}