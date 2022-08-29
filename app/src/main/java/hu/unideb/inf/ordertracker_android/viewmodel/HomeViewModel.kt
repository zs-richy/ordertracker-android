package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import android.util.Base64
import androidx.lifecycle.*
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import hu.unideb.inf.ordertracker_android.network.WelcomeResponse
import hu.unideb.inf.ordertracker_android.util.FileUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    val dataSource = ApiDataSource()

    private val WELCOME_IMAGE_NAME = "welcome.jpg"

    val welcomeResponse = MutableLiveData<WelcomeResponse>(null)
    val welcomeImage = MutableLiveData<String>(null)

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    init {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000)
            val response = dataSource.getWelcomeMessage()
            val file = FileUtils.findPath(getApplication(), FileUtils.Directory.IMAGES, WELCOME_IMAGE_NAME)

            response.imageData?.let {
                FileUtils.writeDataToFile(file, Base64.decode(it, 0))
                welcomeImage.value = WELCOME_IMAGE_NAME
            }

            response.imageData = null

            welcomeResponse.value = response
            _isRefreshing.value = false
        }
    }

}