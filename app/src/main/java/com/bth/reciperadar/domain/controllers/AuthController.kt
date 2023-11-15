package com.bth.reciperadar.domain.controllers

import kotlinx.coroutines.flow.MutableStateFlow

class AuthController {
    private val _authenticated = MutableStateFlow<Boolean>(false)

    val authenticated: MutableStateFlow<Boolean>
        get() = _authenticated

    fun authenticate() {
        _authenticated.value = true
    }
}