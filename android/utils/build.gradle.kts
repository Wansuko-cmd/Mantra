plugins {
    alias(buildLogic.plugins.android.compose.library)
}

android {
    namespace = "com.wsr.android.utils"
}

dependencies {
    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)
}
