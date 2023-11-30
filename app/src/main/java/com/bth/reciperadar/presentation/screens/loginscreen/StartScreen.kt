package com.bth.reciperadar.presentation.screens.loginscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.AuthController
import linearGradient

@Composable
fun StartScreen(authController: AuthController) {
    val gradientBrush = Brush.linearGradient(
        0.3f to Color(0x002C2B2B),
        0.5f to Color(0x8C4D4D4D),
        0.7f to Color(0x002C2B2B),
        angleInDegrees = 30f
    )

    var passwordResetForm by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(gradientBrush)
            .padding(35.dp),
    ) {
        if(passwordResetForm) {
            PasswordResetFIeld(authController = authController) { newValue ->
                passwordResetForm = newValue
            }
        }
        else {
            SignInField(authController = authController)
            ClickableText(
                text = AnnotatedString("Forgot password?"),
                onClick = {
                    passwordResetForm = true
                },
                modifier = Modifier.padding(8.dp),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

@Composable
fun PasswordResetFIeld(authController: AuthController, passwordResetForm: (Boolean) -> Unit) {
    var email by remember { mutableStateOf("") }

    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "RecipeRadar Logo",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )
    Spacer(modifier = Modifier.height(60.dp))

    EmailInputField(email = email, onEmailChange = { email = it })

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            authController.resetPassword(email)
            passwordResetForm(false)
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    ) {
        Text("Reset password")
    }

    Spacer(modifier = Modifier.height(8.dp))

    ClickableText(
        text = AnnotatedString("Back"),
        onClick = {
            passwordResetForm(false)
        },
        modifier = Modifier.padding(8.dp),
        style = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    )
}

@Composable
fun SignInField(authController: AuthController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignIn by remember { mutableStateOf(true) }

    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "RecipeRadar Logo",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )

    Spacer(modifier = Modifier.height(60.dp))

    EmailPasswordInputFields(email = email, password = password, onEmailChange = { email = it }, onPasswordChange = { password = it })

    Spacer(modifier = Modifier.height(16.dp))

    AuthButton(
        onClick = {
            if (isSignIn) {
                authController.authenticate(email, password)
            } else {
                authController.createAccount(email, password)
            }
        },
        text = if (isSignIn) "Sign In" else "Create Account"
    )

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = { isSignIn = !isSignIn },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text(text = if (isSignIn) "Sign Up" else "Back to Sign in")
    }
}

@Composable
fun EmailPasswordInputFields(email: String, password: String, onEmailChange: (String) -> Unit, onPasswordChange: (String) -> Unit) {
    Column {
        EmailInputField(email = email, onEmailChange = onEmailChange)
        PasswordInputField(password = password, onPasswordChange = onPasswordChange)
    }
}

@Composable
fun EmailInputField(email: String, onEmailChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun PasswordInputField(password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    painter = painterResource(id = if (passwordVisibility) R.drawable.baseline_visibilty_24 else R.drawable.outline_visibility_24),
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun AuthButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = text)
    }
}

@Composable
fun IconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .size(24.dp)
    ) {
        content()
    }
}
