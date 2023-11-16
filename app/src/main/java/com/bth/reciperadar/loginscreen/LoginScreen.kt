package com.bth.reciperadar.loginscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.AuthController
import linearGradient

@Composable
fun LoginScreen(authController: AuthController) {
    val gradientBrush = Brush.linearGradient(
        0.3f to Color(0x002C2B2B),
        0.5f to Color(0x8C4D4D4D),
        0.7f to Color(0x002C2B2B),
        angleInDegrees = 30f
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxWidth().background(gradientBrush),
    ) {
        Button(
            onClick = {
                authController.authenticate()
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "RecipeRadar Logo",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontSize = 30.sp,
                    fontWeight = FontWeight(1000),
                    fontStyle = FontStyle(1),
                    text = "Tap to log in",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}