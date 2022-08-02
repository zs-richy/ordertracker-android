package hu.unideb.inf.ordertracker_android.handler

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import hu.unideb.inf.ordertracker_android.model.api.User


enum class SharedPreferenceStorage(name: String) {
    AUTH("auth"), DATA("data")
}


class SharedPreferencesHandler {

    companion object {

        private val USER_TOKEN = "userToken"
        private val USERNAME = "username"
        private val USER_TOKEN_EXPIRES = "userTokenExpires"

        private fun getAuthPreferences(context: Context): SharedPreferences {
            return EncryptedSharedPreferences.create(
                context,
                SharedPreferenceStorage.AUTH.name,
                MasterKey(context, "authKey"),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }


        fun storeAuthDetails(context: Context, user: User) {
            val sharedPreferences = getAuthPreferences(context)

            val editor = sharedPreferences.edit()
            editor.putString(USER_TOKEN, user.token)
            editor.putString(USERNAME, user.userName)
            editor.putLong(USER_TOKEN_EXPIRES, user.userTokenExpire ?: 0)
            editor.apply()

        }

        fun loadAuthDetails(context: Context): User? {
            val sharedPreferences = getAuthPreferences(context)

            val userToken = sharedPreferences.getString(USER_TOKEN, "")
            val username = sharedPreferences.getString(USERNAME, "")
            val expires = sharedPreferences.getLong(USER_TOKEN_EXPIRES, 0)

            if (expires < System.currentTimeMillis()) {
                return null
            }

            if (username.isNullOrEmpty() || userToken.isNullOrEmpty()) {
                return null
            }

            return User(username, userToken, expires)

        }

        fun clearAuthDetails(context: Context) {
            val sharedPreferences = getAuthPreferences(context)

            sharedPreferences.edit().remove(USER_TOKEN).remove(USER_TOKEN_EXPIRES).apply()
        }

        fun getToken(context: Context): String? {
            val sharedPreferences = getAuthPreferences(context)

            return sharedPreferences.getString(USER_TOKEN, "")
        }

        fun getUsername(context: Context): String? {
            val sharedPreferences = getAuthPreferences(context)

            return sharedPreferences.getString(USERNAME, null)
        }

    }

}