plugins {
    alias(buildLogic.plugins.android.compose.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.wsr.android.ui"
}

dependencies {
    implementation(libs.bundles.androidx)
}
