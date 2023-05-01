package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.FetchIsEntryUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paran.presentation.utils.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel
@Inject
constructor(
    private val fetchIsEntryUseCase: FetchIsEntryUseCase,
) : ViewModel() {
    companion object {
        private const val TAG = "SplashActVM"
    }

    private val _routeState = MutableLiveData<SplashState>(SplashState.Init)
    val routeState: LiveData<SplashState>
        get() = _routeState

    fun checkEntry() = CoroutineScope(Dispatchers.IO).launch {
        when (fetchIsEntryUseCase()) {
            true -> _routeState.postValue(SplashState.SignIn)
            false -> _routeState.postValue(SplashState.Intro)
        }
    }

}