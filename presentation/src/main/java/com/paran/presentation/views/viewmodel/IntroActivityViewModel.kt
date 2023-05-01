package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.SaveIsEntryUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paran.presentation.utils.state.IntroState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroActivityViewModel
@Inject
constructor(
    private val saveIsEntryUseCase: SaveIsEntryUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "IntroActVM"
    }

    private val _routeState = MutableLiveData<IntroState>(IntroState.Init)
    val routeState: LiveData<IntroState>
        get() = _routeState

    fun saveIsEntry() = CoroutineScope(Dispatchers.IO).launch {
        saveIsEntryUseCase().onSuccess {
            _routeState.postValue(IntroState.SignIn)
        }.onFailure {
            _routeState.postValue(IntroState.SignIn)
        }
    }

}