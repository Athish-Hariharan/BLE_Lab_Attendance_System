package com.bleattendanceapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AttendanceScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("BLE Attendance Scanning...", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Dashboard")
        }
    }
}
