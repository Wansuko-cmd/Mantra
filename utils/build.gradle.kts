plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
}

android {
    namespace = "com.wsr.utils"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.datetime)
            }
        }
    }
}
