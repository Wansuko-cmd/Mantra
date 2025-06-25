@file:Suppress("UnstableApiUsage")

pluginManagement {
    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("buildLogic") {
            from(files("./gradle/build-logic.versions.toml"))
        }
    }
}

rootProject.name = "Mantra"

// APP
include(":android:app")
include(":android:assistant")
include(":android:memo")
include(":android:utils")

// TODO
include(":todo:domain")
include(":todo:usecase")
include(":todo:repository")
include(":todo:controller")

// Other
include(":utils")
