package com.example.cloudnotes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudnotes.screens.HomeScreen
import com.example.cloudnotes.screens.LoginScreen
import com.example.cloudnotes.viewmodel.NotesViewModel


@Composable
fun NotesApp(viewModel: NotesViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current
    if (user != null) {
        HomeScreen(viewModel = viewModel, onLogout = { viewModel.logout() })
    } else {
        LoginScreen(
            onLogin = { e, p -> viewModel.login(e, p) { err -> Toast.makeText(context, err, Toast.LENGTH_LONG).show() } },
            onSignUp = { e, p -> viewModel.signUp(e, p) { err -> Toast.makeText(context, err, Toast.LENGTH_LONG).show() } }
        )
    }
}