package com.bth.reciperadar.domain.controllers

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.bth.reciperadar.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow

class AuthController(val applicationContext: Context) {
    private val _authenticated = MutableStateFlow<Boolean>(false)
    private lateinit var _account: Auth0
    private var _accessToken: String? = null

    val authenticated: MutableStateFlow<Boolean>
        get() = _authenticated

    val account: Auth0
        get() = _account

    val accessToken: String?
        get() = _accessToken

    fun authenticate() {
        _account = Auth0(
            BuildConfig.auth0ClientId,
            BuildConfig.auth0Domain
        )

        WebAuthProvider.login(account)
            .withScheme("https")
            .withScope("openid profile email")
            .start(applicationContext, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                }

                override fun onSuccess(result: Credentials) {
                    _accessToken = result.accessToken
                    _authenticated.value = true
                }
            })
    }
}