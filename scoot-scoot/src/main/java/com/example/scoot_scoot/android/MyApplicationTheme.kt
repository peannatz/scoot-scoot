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
            background = Color(0xFF002830),
            primary = Color(0xFF127585),
            primaryVariant = Color(0xFFD1BCFF),
            secondary = Color(0xFF259BAA),
            onPrimary = Color(0xFF003238),
            onSecondary = Color(0xFFFFFFFF)
        )
    } else {
        lightColors(
            background = Color(0xFF6DCBF1),
            primary = Color(0xFF127585),
            primaryVariant = Color(0xFF684FA3),
            secondary = Color(0xFF54DBC8),
            onPrimary = Color(0xFF9CF4FF),
            onSecondary = Color(0xFF0A5F6D)
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
