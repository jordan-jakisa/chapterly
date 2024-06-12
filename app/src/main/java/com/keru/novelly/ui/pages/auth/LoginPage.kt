package com.keru.novelly.ui.pages.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keru.novelly.ui.navigation.Routes
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavController,
    vm: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var emailError by rememberSaveable {
        mutableStateOf(false)
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordError by rememberSaveable {
        mutableStateOf(false)
    }
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by vm.uiState.collectAsState()
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = uiState,
        block = {
            if (uiState.isSuccess) {
                isLoading = false
                navController.navigate(Routes.Home.path) {
                    popUpTo(Routes.LoginPage.path) { inclusive = true }
                }
                Toast.makeText(context, "Sign in success!", Toast.LENGTH_SHORT).show()
            }
            if (uiState.response.isNotEmpty()) {
                snackBarHostState.showSnackbar(uiState.response)
                isLoading = false
            }
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)

        }, modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LargeTopAppBar(title = { Text(text = "Sign In") }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ), keyboardActions = KeyboardActions {
                defaultKeyboardAction(ImeAction.Next)

            }, value = email, onValueChange = {
                email = it
                emailError = false

            }, label = {
                Text(text = "E-mail")
            }, modifier = Modifier.fillMaxWidth(), isError = emailError, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }, shape = RoundedCornerShape(30)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    defaultKeyboardAction(ImeAction.Done)
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false

                },
                label = {
                    Text(text = "Password")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconToggleButton(checked = isPasswordVisible, onCheckedChange = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(30)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        when {
                            email.isEmpty() -> {
                                emailError = true
                                scope.launch {
                                    snackBarHostState.showSnackbar("Email can not be empty.")
                                }
                            }

                            !email.trim().contains("@") -> {
                                emailError = true
                                scope.launch {
                                    snackBarHostState.showSnackbar("Badly formatted email")
                                }
                            }

                            password.trim().length < 6 -> {
                                passwordError = true
                                scope.launch {
                                    snackBarHostState.showSnackbar("Password has to be least 6 characters.")
                                }
                            }

                            else -> {
                                vm.signInUser(email.trim(), password.trim())
                                isLoading = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(.5f)
                ) {
                    Text(text = "Sign in", modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = {
                navController.navigate(Routes.RegisterPage.path)

            }) {
                Text(modifier = Modifier, text = buildAnnotatedString {
                    append("Do not have an account? ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Sign Up")
                    }
                })
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(
                onClick = {
                    navController.navigate(Routes.RecoverPasswordPage.path)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Forgot your password?",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

        }
    }
}