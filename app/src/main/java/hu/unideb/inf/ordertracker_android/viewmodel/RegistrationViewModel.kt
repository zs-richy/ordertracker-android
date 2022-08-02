package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import hu.unideb.inf.ordertracker_android.network.SignupRequest
import hu.unideb.inf.ordertracker_android.network.SignupResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

    enum class RegistrationStatus {
        PROGRESS,
        SUCCESS,
        ERROR
    }

    val dataSource = ApiDataSource()

    private val _registrationStatus = MutableSharedFlow<RegistrationStatus>()
    val registrationStatus: SharedFlow<RegistrationStatus> = _registrationStatus

    val username = MutableLiveData("")

    val password = MutableLiveData("")

    var isSignupInProgress: Boolean = false

    fun signUp() {
        if (isSignupInProgress) return
        isSignupInProgress = true

        viewModelScope.launch {
            _registrationStatus.emit(RegistrationStatus.PROGRESS)

            val request = SignupRequest().also {
                it.username = username.value
                it.password = password.value
            }

            val response = dataSource.signUp(request)
            handleSignUpResponse(response)
        }
    }

    private fun handleSignUpResponse(signupResponse: SignupResponse) {
        isSignupInProgress = false
        viewModelScope.launch {
            if (signupResponse.succeeded) {
                _registrationStatus.emit(RegistrationStatus.SUCCESS)
            } else {
                _registrationStatus.emit(RegistrationStatus.ERROR)
            }
        }
    }

}