package com.paran.presentation.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseETActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
): AppCompatActivity(layoutResId) {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun init(savedInstanceState: Bundle?)
}