plugins {
    alias(buildLogic.plugins.android.compose.library)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.wsr.android.memo"
}

dependencies {
    implementation(projects.android.utils)

    implementation(projects.todo.controller)

    implementation(libs.kotlin.serialization)
    implementation(libs.bundles.androidx)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}
