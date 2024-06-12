package com.keru.novelly.ui.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var isEmailError by rememberSaveable {
        mutableStateOf(false)
    }

    var isSending by rememberSaveable {
        mutableStateOf(false)
    }
    var isError by rememberSaveable {
        mutableStateOf(false)
    }
    var isSuccess by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
            Text(text = "Recover password")
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        })
    }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Forgot your password? No worries! Enter your email address below, " + "and we'll send you a link to reset your password. " + "Get ready to regain access to your account and continue your " + "amazing journey with us! 😊",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(value = email, onValueChange = {
                    email = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), label = {
                    Text(text = "Email")
                }, placeholder = {
                    Text(text = "example@email.com")
                }, isError = isEmailError,
                    shape = RoundedCornerShape(50)
                )

                Button(onClick = {
                    if (email.isNotEmpty() && email.contains("@")) {
                        isSending = true
                    } else {
                        isEmailError = true
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Send")
                }
            }
        }
    }
}