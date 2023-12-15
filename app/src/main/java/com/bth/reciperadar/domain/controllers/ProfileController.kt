package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.DietaryInfoRepository
import com.bth.reciperadar.data.repositories.ProfileRepository

class ProfileController(
    private val dietaryInfoController: DietaryInfoController,
    private val authController: AuthController,
    private val profileRepository: ProfileRepository
) {

}