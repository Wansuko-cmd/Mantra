plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.wsr.todo.repository"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.todo.domain)

                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)

                implementation(libs.datastore.core.okio)
            }
        }
    }
}
