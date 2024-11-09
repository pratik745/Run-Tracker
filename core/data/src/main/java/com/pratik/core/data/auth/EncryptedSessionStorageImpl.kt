package com.pratik.core.data.auth

import android.content.SharedPreferences
import com.pratik.core.domain.authSession.AuthInfo
import com.pratik.core.domain.authSession.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorageImpl(
    private val sharedPreferences: SharedPreferences
): SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val authInfoJson = sharedPreferences.getString(KEY_AUTH_INFO,null)
            authInfoJson?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(Dispatchers.IO) {
            info?.let { authInfo ->
                val authInfoJson = Json.encodeToString(authInfo.toAuthInfoSerializable())
                sharedPreferences
                    .edit()
                    .putString(KEY_AUTH_INFO,authInfoJson)
                    .apply()
            } ?: run {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
            }
        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}