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
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    navController: NavController,
    authController: AuthController,
    profileController: ProfileController) {

    var profile by remember { mutableStateOf<ProfileViewModel?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val profileModel = profileController.getProfile()
            profile = profileModel?.toViewModel()
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
                text = "My Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))


            IconButton(
                onClick = {
                    navController.navigate(Screen.EditProfileScreen.route)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 5.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")
            }
        }

        Text("*PROFILE PICTURE HERE*", modifier = Modifier.align(CenterHorizontally))

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        Text("Username: ${profile?.username}",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text("Email: ${profile?.email}",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        if (profile?.dietaryInfo != null) {
            if (profile?.dietaryInfo!!.isNotEmpty()) {
                Text("Dietary Preferences:",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                profile?.dietaryInfo?.forEach { dietaryInfo ->
                    Text(dietaryInfo.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Button(onClick = {
            authController.logout()
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}