package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import androidx.lifecycle.*
import hu.unideb.inf.ordertracker_android.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertracker_android.model.api.User
import hu.unideb.inf.ordertracker_android.network.BackendApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val user = MutableLiveData<User?>(null)
    val loginState = MutableSharedFlow<String>()

    init {
        BackendApi.initRetrofitService()
    }


    fun updateSessionState() {
        user.value = SharedPreferencesHandler.loadAuthDetails(getApplication())
        user.value?.token?.let { token ->
            BackendApi.token = token
        }

        viewModelScope.launch {
            loginState.emit("login")
        }
    }

    fun clearUserData() {
        user.value = null
        SharedPreferencesHandler.clearAuthDetails(getApplication())
    }

}