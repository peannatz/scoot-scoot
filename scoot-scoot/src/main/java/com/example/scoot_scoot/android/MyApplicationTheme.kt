package com.example.scoot_scoot.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            background = Color(0xFF173130),
            primary = Color(0xff0b6966),
            primaryVariant = Color(0xff0c4d49),
            secondary = Color(0xFFa40043),
            onPrimary = Color(0xFF81CBCE),
            onSecondary = Color(0xFFFDE0F6)
        )
    } else {
        lightColors(
            background = Color(0xFFE1F2F3),
            primary = Color(0xFF089696),
            primaryVariant = Color(0xff0b6966),
            secondary = Color(0xFFF278DC),
            onPrimary = Color(0xff0c4d49),
            onSecondary = Color(0xFFE1F2F3)
        )
    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
