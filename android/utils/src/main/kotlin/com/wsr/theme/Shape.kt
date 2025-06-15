package com.wsr.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val MantraTheme.shape get() = PrimitiveShape

object PrimitiveShape {
    val Small = RoundedCornerShape(8.0.dp)
    val Medium = RoundedCornerShape(16.0.dp)
    val Large = RoundedCornerShape(24.0.dp)
    val Full = RoundedCornerShape(100)
}
