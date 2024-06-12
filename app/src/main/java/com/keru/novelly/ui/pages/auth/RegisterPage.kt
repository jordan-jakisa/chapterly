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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keru.novelly.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    navController: NavController, vm: SignupViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
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

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPasswordError by rememberSaveable {
        mutableStateOf(false)
    }
    var isConfirmPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isAgreedTerms by rememberSaveable {
        mutableStateOf(true)
    }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState) {
        if (uiState.success) {
            navController.navigate(Routes.More.path)
            Toast.makeText(context, "Sing up success", Toast.LENGTH_SHORT).show()
        }

        if (uiState.response.isNotEmpty()) {
            scope.launch {
                snackBarHostState.showSnackbar(uiState.response)
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(snackBarHostState)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LargeTopAppBar(
                title = { Text(text = "Sign Up") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

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

            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    defaultKeyboardAction(ImeAction.Next)
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
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(30)
            )

            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    defaultKeyboardAction(ImeAction.Done)
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = false

                },
                label = {
                    Text(text = "Confirm Password")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = confirmPasswordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconToggleButton(checked = isConfirmPasswordVisible, onCheckedChange = {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                    }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(30)
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        when {
                            password != confirmPassword -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Passwords are not matching!")
                                }
                            }

                            password.length < 6 -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Password should be atleast 6 characters!")
                                }
                            }

                            email.isEmpty() -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Email cannot be blank!")
                                }
                            }

                            password.isEmpty() || confirmPassword.isEmpty() -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Passwords cannot be blank!")
                                }
                            }

                            !email.trim().contains("@") -> {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Invalid email format!")
                                }
                            }

                            else -> {
                                if (email.trim()
                                        .contains("@") && email.isNotEmpty() && password == confirmPassword
                                ) {
                                    vm.registerUser(email.trim(), password.trim())
                                }
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth(.5f)
                ) {
                    Text(text = "Sign Up", modifier = Modifier.padding(8.dp))
                }
            }

            TextButton(onClick = {
                navController.navigate(Routes.LoginPage.path)

            }, modifier = Modifier.padding(vertical = 16.dp)) {
                Text(modifier = Modifier, text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Sign In")
                    }
                })
            }

        }
    }

}