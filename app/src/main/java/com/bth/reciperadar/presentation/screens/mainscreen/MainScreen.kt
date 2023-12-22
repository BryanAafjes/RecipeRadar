package com.bth.reciperadar.presentation.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.screens.recipe.RecipeListView
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.viewmodels.ProfileViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    navController: NavController,
    authController: AuthController,
    recipeController: RecipeController,
    profileController: ProfileController
) {
    var profile by remember {
        mutableStateOf<ProfileViewModel?>(null)
    }

    var searchQuery by remember {
        mutableStateOf<String?>("")
    }

    val state = rememberScrollState()

    var recipes by remember { mutableStateOf<List<RecipeViewModel>>(emptyList()) }

    var showEmailVerifyNotification by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authController.auth.currentUser?.reload()
        showEmailVerifyNotification = authController.auth.currentUser?.isEmailVerified == false

        recipes = withContext(Dispatchers.IO) {
            val recipeModels = recipeController.getRecipes()
            recipeModels.map{ it.toViewModel() }
        }

        profile = profileController.getProfile()?.toViewModel()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .verticalScroll(state)
    ) {
        Row(modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)) {
            Text(
                text = "Hi, ${profile?.username ?: "User"} \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).align(CenterVertically)
            )
            if (profile != null) {
                if(profile!!.pictureDownloadUri != null) {
                    Image(
                        painter = rememberImagePainter(data = profile!!.pictureDownloadUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(CircleShape)
                            .align(CenterVertically)
                    )
                }
            }
        }

        if (showEmailVerifyNotification) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.width(250.dp)) {
                        Text(
                            text = "Don’t forget to confirm your account’s email address!",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Column {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { showEmailVerifyNotification = false }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        OutlinedTextField(
            value = searchQuery ?: "",
            onValueChange = {
                searchQuery = it
            },
            label = { Text("Search by recipe name") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    if (searchQuery == "") {
                        searchQuery = null
                    }
                    navController.navigate(
                        Screen.RecipeSearchScreen.withArgs(
                            mapOf(
                                "searchQuery" to searchQuery,
                                "searchWithIngredients" to false
                            )
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate(
                Screen.RecipeSearchScreen.withArgs(
                    mapOf(
                        "searchQuery" to null,
                        "searchWithIngredients" to true
                    )
                )
            )        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Look for a recipe with your ingredients")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Recipes",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        RecipeListView(recipes = recipes, navController = navController)
    }
}