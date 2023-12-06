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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.screens.recipe.RecipeListView
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    navController: NavController,
    authController: AuthController,
    recipeController: RecipeController
) {
    var text by remember {
        mutableStateOf("")
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var recipes by remember { mutableStateOf<List<RecipeViewModel>>(emptyList()) }

    var showEmailVerifyNotification by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authController.auth.currentUser?.reload()
        showEmailVerifyNotification = authController.auth.currentUser?.isEmailVerified == false

        recipes = withContext(Dispatchers.IO) {
            val recipeModels = recipeController.getRecipes()
            recipeModels.map{ it.toViewModel() }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "RecipeRadar Logo",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        if (showEmailVerifyNotification) {
            Spacer(modifier = Modifier.height(20.dp))
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
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navController.navigate(Screen.RecipeSearchScreen.withArgs(searchQuery))
        },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = "Search Recipes")
        }
        Spacer(modifier = Modifier.height(20.dp))
        RecipeListView(recipes = recipes)
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
                navController.navigate(Screen.DetailScreen.withArgs(text))
            },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = "To DetailScreen")
        }
    }
}