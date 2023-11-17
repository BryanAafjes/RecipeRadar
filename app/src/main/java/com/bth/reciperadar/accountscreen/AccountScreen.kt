package com.bth.reciperadar.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.domain.controllers.AuthController

@Composable
fun AccountScreen(authController: AuthController) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Text("This is the account screen!", color = MaterialTheme.colorScheme.onBackground)
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 15.dp)
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