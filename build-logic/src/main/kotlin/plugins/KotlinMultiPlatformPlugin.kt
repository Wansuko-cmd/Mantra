package plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import plugins.ext.alias
import plugins.ext.configureCommonAndroidSetting
import plugins.ext.getPlugin
import plugins.ext.kotlinMultiplatform
import plugins.ext.libs

class KotlinMultiPlatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.getPlugin("kotlin"))
                alias(libs.getPlugin("android.library"))
                alias(libs.getPlugin("ktlint"))
            }

            @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
            kotlinMultiplatform {
                applyDefaultHierarchyTemplate()

                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }

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
