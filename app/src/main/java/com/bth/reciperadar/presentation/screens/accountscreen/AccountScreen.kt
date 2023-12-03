package com.bth.reciperadar.presentation.screens.accountscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.presentation.screens.listscreen.hexToComposeColor

@Composable
fun UserProfileInfoField() {

    // TODO

}

@Composable
fun AccountScreen(authController: AuthController) {
    Column( modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            text = "My profile", fontSize = 40.sp, fontWeight = FontWeight(800)
        )
        Image( modifier = Modifier
            .size(200.dp, 200.dp)
            .clip(CircleShape)
            .border( width = 10.dp, color = Color.White, shape = CircleShape),
            painter = painterResource(id = R.drawable.happy_fox),
            contentDescription = "Happy_Fox")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(hexToComposeColor("#485E9D"))
                //.border( width = 10.dp, shape = RoundedCornerShape(15.dp), color = Color.White)
        ) {
            item {
                UserProfileInfoField()
                UserProfileInfoField()
                UserProfileInfoField()
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(hexToComposeColor("#485E9D"))
                .clip(RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {
            Button(
                onClick = { authController.logout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            ) { Text(text = "Logout") }
        }
    }
}