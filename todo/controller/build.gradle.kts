plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
}

android {
    namespace = "com.wsr.todo.controller"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.todo.domain)
                implementation(projects.todo.usecase)
                implementation(projects.todo.repository)

                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)

                implementation(libs.mcp)
            }
        }
    }
}
