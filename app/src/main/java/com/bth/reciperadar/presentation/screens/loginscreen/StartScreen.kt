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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.google.firebase.auth.FirebaseAuth
import linearGradient
import java.util.regex.Pattern

class FieldState(private val _data: String = "", private val _isInvalid: Boolean = true) {
    val data: String
        get() = _data

    val isInvalid: Boolean
        get() = data.isBlank() || _isInvalid
}

class Credentials(private val _email: FieldState, private val _password: FieldState) {
    val email: String
        get() = _email.data

    val password: String
        get() = _password.data

    val areValid: Boolean
        get() = !(_email.isInvalid || _password.isInvalid)
}

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
            PasswordResetField(authController = authController) { newValue ->
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
fun PasswordResetField(authController: AuthController, passwordResetForm: (Boolean) -> Unit) {
    var showErrorPopup by remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "RecipeRadar Logo",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )
    Spacer(modifier = Modifier.height(60.dp))

    val email = emailInputField()

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            if (!email.isInvalid) {
                authController.resetPassword(email.data)
                passwordResetForm(false)
            } else {
                showErrorPopup = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Text("Reset password")

        // TODO: Improve the error popup
        DropdownMenu(expanded = showErrorPopup, onDismissRequest = { showErrorPopup = false }) {
            DropdownMenuItem(
                text = { Text("The provided email is invalid") },
                onClick = { showErrorPopup = false }
            )
        }
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
    var isSignIn by remember { mutableStateOf(true) }

    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "RecipeRadar Logo",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )

    Spacer(modifier = Modifier.height(60.dp))

    val credentials = credentialsInputFields(isSignIn)

    Spacer(modifier = Modifier.height(16.dp))

    AuthButton(
        authController = authController,
        credentials = credentials,
        isSignIn = isSignIn
    )

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = { isSignIn = !isSignIn },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
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
fun credentialsInputFields(isSignIn: Boolean): Credentials {
    var emailState = FieldState()
    var passwordState = FieldState()

    Column {
        emailState = emailInputField()
        passwordState = passwordInputField(isSignIn)
    }

    return Credentials(emailState, passwordState)
}

@Composable
fun emailInputField(): FieldState {
    val errorText = "Invalid email"
    var email by remember { mutableStateOf("") }
    var isInvalid by remember { mutableStateOf(false) }

    fun validate(email: String) {
        isInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    TextField(
        value = email,
        onValueChange = {
            email = it
            validate(email)
        },
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        singleLine = true,
        supportingText = {
            if (isInvalid) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardActions = KeyboardActions { validate(email) }
    )

    return FieldState(email, isInvalid)
}

@Composable
fun passwordInputField(isSignIn: Boolean): FieldState {
    val errorText = "Invalid password"
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isInvalid by remember { mutableStateOf(false) }

    val passwordRegex = Pattern.compile("^" +
            "(?=.*[0-9])" +         // At least 1 digit
            "(?=.*[a-z])" +         // At least 1 lower case letter
            "(?=.*[A-Z])" +         // At least 1 upper case letter
            "(?=.*[a-zA-Z])" +      // Any letter
            "(?=.*[@#$%^&+=])" +    // At least 1 special character
            "(?=\\S+$)" +           // No white spaces
            ".{8,}" +               // At least 8 characters
            "$"
    );

    fun validate(password: String) {
        // Only check the password strength at Sign Up
        isInvalid = if (!isSignIn) {
            !passwordRegex.matcher(password).matches()
        } else {
            false
        }
    }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            validate(password)
        },
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
            .padding(bottom = 8.dp),
        singleLine = true,
        supportingText = {
            if (isInvalid) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    // TODO: Improve the error message by adding a password requirement checklist panel
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardActions = KeyboardActions { validate(password) }
    )

    return FieldState(password, isInvalid)
}

@Composable
fun AuthButton(authController: AuthController, credentials: Credentials, isSignIn: Boolean) {
    var showErrorPopup by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (credentials.areValid) {
                if (isSignIn) {
                    authController.authenticate(credentials.email, credentials.password)
                } else {
                    authController.createAccount(credentials.email, credentials.password)
                }
            } else {
                showErrorPopup = true
            }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = if (isSignIn) "Sign In" else "Create Account")

        // TODO: Improve the error popup
        DropdownMenu(expanded = showErrorPopup, onDismissRequest = { showErrorPopup = false }) {
            DropdownMenuItem(
                text = { Text("The email or password is invalid") },
                onClick = { showErrorPopup = false }
            )
        }
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
