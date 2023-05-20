package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.FetchAccessTokenUseCase
import ajou.paran.domain.usecase.FetchIsEntryUseCase
import ajou.paran.domain.usecase.FetchUserEmailUseCase
import ajou.paran.domain.usecase.FindAllPlannersByUserUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paran.presentation.utils.state.SignInState
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
    private val fetchAccessTokenUseCase: FetchAccessTokenUseCase,
    private val fetchUserEmailUseCase: FetchUserEmailUseCase,
    private val findAllPlannersByUserUseCase: FindAllPlannersByUserUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "SplashActVM"
    }

    private val _routeState = MutableLiveData<SplashState>(SplashState.Init)
    val routeState: LiveData<SplashState>
        get() = _routeState

    fun checkToken() = CoroutineScope(Dispatchers.Default).launch {
        when (fetchAccessTokenUseCase().isEmpty()) {
            true -> checkEntry()
            false -> syncDBToServer()
        }
    }

    private fun checkEntry() = CoroutineScope(Dispatchers.IO).launch {
        when (fetchIsEntryUseCase()) {
            true -> _routeState.postValue(SplashState.SignIn)
            false -> _routeState.postValue(SplashState.Intro)
        }
    }

    private fun syncDBToServer() = CoroutineScope(Dispatchers.IO).launch {
        findAllPlannersByUserUseCase(fetchUserEmailUseCase())
            .onSuccess { _routeState.postValue(SplashState.Home) }
            .onFailure { checkEntry() }
    }

}