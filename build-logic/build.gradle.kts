plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.android)
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
