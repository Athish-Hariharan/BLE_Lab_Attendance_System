package com.bleattendanceapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bleattendanceapp.data.UserPreferences
import com.bleattendanceapp.screens.RegisterScreen
import com.bleattendanceapp.ui.theme.BLEAttendanceAppTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If already registered, go to Home
        if (UserPreferences.isUserRegistered(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        setContent {
            BLEAttendanceAppTheme {
                RegisterScreen(
                    onRegisterSuccess = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
