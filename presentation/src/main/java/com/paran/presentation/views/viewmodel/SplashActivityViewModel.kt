package com.paran.presentation.views.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel
@Inject
constructor(

) : ViewModel() {
    companion object {
        private const val TAG = "SplashActVM"
    }


}