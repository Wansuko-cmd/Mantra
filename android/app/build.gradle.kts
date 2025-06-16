@file:Suppress("UnstableApiUsage")

plugins {
    alias(buildLogic.plugins.android.compose.application)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.wsr.app"

    defaultConfig {
        applicationId = "com.wsr"
        versionCode = 1
        versionName = "0.01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFiles.add(file("proguard-rules.pro"))
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.android.utils)

    implementation(projects.todo.controller)

    implementation(libs.kotlin.serialization)
    implementation(libs.bundles.androidx)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}
