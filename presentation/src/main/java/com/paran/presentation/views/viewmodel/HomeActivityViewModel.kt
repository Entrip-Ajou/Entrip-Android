package com.paran.presentation.views.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel
@Inject
constructor(

) : ViewModel() {
    companion object {
        private const val TAG = "HomeActVM"
    }


}