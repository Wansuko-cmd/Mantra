plugins {
    `kotlin-dsl`
    alias(libs.plugins.ktlint)
}

dependencies {
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.android)
    implementation(libs.gradle.roborazzi)
}

gradlePlugin {
    plugins {
        register("androidComposeApplication") {
            id = "com.wsr.compose.application"
            implementationClass = "plugins.AndroidComposeApplicationPlugin"
        }
        register("androidComposeLibrary") {
            id = "com.wsr.compose.library"
            implementationClass = "plugins.AndroidComposeLibraryPlugin"
        }
        register("kotlinMultiPlatform") {
            id = "com.wsr.multiplatform"
            implementationClass = "plugins.KotlinMultiPlatformPlugin"
        }
    }
}
