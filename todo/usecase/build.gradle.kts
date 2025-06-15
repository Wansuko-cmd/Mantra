plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
}

android {
    namespace = "com.wsr.todo.usecase"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.todo.domain)

                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)
            }
        }
    }
}
