package plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugins.ext.alias
import plugins.ext.configureCommonAndroidSetting
import plugins.ext.getPlugin
import plugins.ext.libs

class KotlinMultiPlatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.getPlugin("kotlin"))
                alias(libs.getPlugin("android.library"))
            }

            @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
            kotlin {
                applyDefaultHierarchyTemplate()

                androidTarget()

                // iOSビルド用
//                iosX64()
//                iosArm64()
//                iosSimulatorArm64()
            }

            extensions.configure<LibraryExtension> {
                configureCommonAndroidSetting(this)
            }
        }
    }
}

private fun Project.kotlin(configure: Action<KotlinMultiplatformExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)
