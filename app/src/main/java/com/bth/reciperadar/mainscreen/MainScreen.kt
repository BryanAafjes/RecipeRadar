package com.bth.reciperadar.mainscreen

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.bth.reciperadar.screen.Screen
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MainScreen(navController: NavController, authController: AuthController) {
    var text by remember {
        mutableStateOf("")
    }

    var showEmailVerifyNotification by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authController.auth.currentUser?.reload()
        showEmailVerifyNotification = authController.auth.currentUser?.isEmailVerified == false
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