package com.bleattendanceapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = shape,
        elevation = elevation
    ) {
        Text(text)
    }
}
