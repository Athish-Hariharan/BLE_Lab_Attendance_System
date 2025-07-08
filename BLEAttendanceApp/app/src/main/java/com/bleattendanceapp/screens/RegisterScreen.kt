package com.bleattendanceapp.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bleattendanceapp.data.UserPreferences
import com.bleattendanceapp.data.UUIDAllocator


@Composable
fun RegisterScreen(
    context: Context = LocalContext.current,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register for Attendance",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rollNumber,
            onValueChange = { rollNumber = it },
            label = { Text("Roll Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorText.isNotEmpty()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (name.isBlank() || rollNumber.isBlank()) {
                    errorText = "Please fill in all fields"
                } else {
                    val uuid = UUIDAllocator.assignUUID(context)
                    UserPreferences.saveUserDetails(context, name, rollNumber, uuid)
                    UserPreferences.setUserRegistered(context, true)
                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register & Start")
        }
    }
}
