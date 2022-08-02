package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.unideb.inf.ordertracker_android.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertracker_android.model.api.User
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import hu.unideb.inf.ordertracker_android.network.LoginRequest
import hu.unideb.inf.ordertracker_android.network.LoginResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    enum class LoginStatus {
        PROGRESS,
        ERROR,
        SUCCESS
    }

    val dataSource = ApiDataSource()

    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val _loginStatus = MutableSharedFlow<LoginStatus>()
    val loginStatus: SharedFlow<LoginStatus> = _loginStatus

    var isLoginInProgress: Boolean = false

    fun login() {
        if (isLoginInProgress) return

        viewModelScope.launch {
            _loginStatus.emit(LoginStatus.PROGRESS)

            delay(2500)
            dataSource.authenticateClient(LoginRequest().also {
                it.username = username.value
                it.password = password.value
            }).also {
                handleLoginResponse(it)
                _loginStatus.emit(LoginStatus.SUCCESS)
            }
        }

    }

    private fun handleLoginResponse(response: LoginResponse) {
        response.username?.let { username ->
            User(username, response.token ?: "", response.expires ?: 0).also {
                SharedPreferencesHandler.storeAuthDetails(getApplication(), it)
            }
        } ?: run {
            viewModelScope.launch {
                _loginStatus.emit(LoginStatus.ERROR)
            }
        }
    }

}