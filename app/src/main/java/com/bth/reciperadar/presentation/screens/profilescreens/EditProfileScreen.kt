package com.bth.reciperadar.presentation.screens.profilescreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.DietaryInfoController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.presentation.screens.recipe.DietaryInfoAccordion
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.ProfileViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditProfileScreen(
    navController: NavController,
    authController: AuthController,
    profileController: ProfileController,
    dietaryInfoController: DietaryInfoController,
) {
    var profile by remember { mutableStateOf<ProfileViewModel?>(null) }
    var dietaryInfoList by remember { mutableStateOf<List<DietaryInfoViewModel>>(emptyList()) }
    var selectedDietaryInfoList by remember { mutableStateOf<List<DietaryInfoViewModel>>(emptyList()) }
    var isDietaryInfoDropdownVisible by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val profileModel = profileController.getProfile()
            profile = profileModel?.toViewModel()

            if (profile == null) {
                profile = ProfileViewModel(
                    id = "",
                    userId = "",
                    picturePath = "",
                    username = "",
                    dietaryInfo = emptyList()
                )
            }

            val dietaryModels = dietaryInfoController.getDietaryInfo()
            dietaryInfoList = dietaryModels.map { it.toViewModel() }

            selectedDietaryInfoList = profile?.dietaryInfo ?: emptyList()
            username = profile?.username ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (profile != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                profile!!.dietaryInfo = selectedDietaryInfoList
                                profile!!.username = username

                                profileController.createOrUpdateProfile(profile!!.toDomain())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    navController.navigate(Screen.ProfileScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp).width(120.dp)
            ) {
                Text(text = "Save")
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        if (profile != null) {
            TextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDietaryInfoDropdownVisible = !isDietaryInfoDropdownVisible }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Selected Diets",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        if (isDietaryInfoDropdownVisible) {
            DietaryInfoAccordion(
                dietaryInfoList = dietaryInfoList,
                selectedDietaryInfo = selectedDietaryInfoList,
                onDietaryInfoSelect = { selectedDietaryInfo ->
                    selectedDietaryInfoList = if (selectedDietaryInfoList.contains(selectedDietaryInfo)) {
                        selectedDietaryInfoList.minus(selectedDietaryInfo)
                    } else {
                        selectedDietaryInfoList.plus(selectedDietaryInfo)
                    }
                }
            )
        }

    }
}