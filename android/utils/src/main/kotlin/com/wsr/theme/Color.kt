package com.wsr.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MantraTheme.colorScheme
    @Composable get() = MaterialTheme.colorScheme.copy(
        primary = PrimitiveColor.PrimaryGreen80,
        onPrimary = PrimitiveColor.PrimaryGreen10,
        secondary = PrimitiveColor.SecondaryBeige50,
        onSecondary = PrimitiveColor.SecondaryBeige10,
        primaryContainer = PrimitiveColor.PrimaryGreen30,
        background = PrimitiveColor.White100,
        surface = PrimitiveColor.PrimaryGreen40,
        onSurface = PrimitiveColor.Black100,
        error = PrimitiveColor.Red100,
        onError = PrimitiveColor.Red10,
    )

val MantraTheme.colors get() = PrimitiveColor

object PrimitiveColor {
    val PrimaryGreen100 = Color(0xff525735)
    val PrimaryGreen90 = Color(0xff737842)
    val PrimaryGreen80 = Color(0xff888b4a)
    val PrimaryGreen70 = Color(0xff9d9e54)
    val PrimaryGreen60 = Color(0xffadad5c)
    val PrimaryGreen50 = Color(0xffb9b872)
    val PrimaryGreen40 = Color(0xffc5c489)
    val PrimaryGreen30 = Color(0xffd5d4a9)
    val PrimaryGreen20 = Color(0xffe6e5c9)
    val PrimaryGreen10 = Color(0xfff5f5ea)

    val SecondaryBeige100 = Color(0xff35240d)
    val SecondaryBeige90 = Color(0xff423218)
    val SecondaryBeige80 = Color(0xff4f3f20)
    val SecondaryBeige70 = Color(0xff5c4b29)
    val SecondaryBeige60 = Color(0xff66552f)
    val SecondaryBeige50 = Color(0xff7e6d4c)
    val SecondaryBeige40 = Color(0xff95866a)
    val SecondaryBeige30 = Color(0xffb6a892)
    val SecondaryBeige20 = Color(0xffd5cbb9)
    val SecondaryBeige10 = Color(0xfff0ebdd)

    val FieldBeige100 = Color(0xff22211b)
    val FieldBeige90 = Color(0xff43423b)
    val FieldBeige80 = Color(0xff62615a)
    val FieldBeige70 = Color(0xff76756e)
    val FieldBeige60 = Color(0xffa09e96)
    val FieldBeige50 = Color(0xffbfbdb5)
    val FieldBeige40 = Color(0xffe2e0d8)
    val FieldBeige30 = Color(0xfff0eee6)
    val FieldBeige20 = Color(0xfff7f5ed)
    val FieldBeige10 = Color(0xfffcfaf2)

    val Black100 = Color(0xff000000)
    val Black90 = Color(0xff262626)
    val Black80 = Color(0xff434343)
    val Black70 = Color(0xff555555)
    val Black60 = Color(0xff7b7b7b)
    val Black50 = Color(0xff9d9d9d)
    val Black40 = Color(0xffc4c4c4)
    val Black30 = Color(0xffd9d9d9)
    val Black20 = Color(0xffe9e9e9)
    val Black10 = Color(0xfff5f5f5)

    val White100 = Color(0xffffffff)

    val Red100 = Color(0xffff0000)
    val Red90 = Color(0xffff2626)
    val Red80 = Color(0xffff3333)
    val Red70 = Color(0xffff4d4d)
    val Red60 = Color(0xffff6666)
    val Red50 = Color(0xffff8080)
    val Red40 = Color(0xffff9999)
    val Red30 = Color(0xffffb3b3)
    val Red20 = Color(0xffffcccc)
    val Red10 = Color(0xffffe6e6)

    val LightBlue100 = Color(0xff004aad)
    val LightBlue90 = Color(0xff0069cb)
    val LightBlue80 = Color(0xff007add)
    val LightBlue70 = Color(0xff028df1)
    val LightBlue60 = Color(0xff009bff)
    val LightBlue50 = Color(0xff39a9ff)
    val LightBlue40 = Color(0xff60b9ff)
    val LightBlue30 = Color(0xff8fcdff)
    val LightBlue20 = Color(0xffbbe0fe)
    val LightBlue10 = Color(0xffe3f3fe)
}
