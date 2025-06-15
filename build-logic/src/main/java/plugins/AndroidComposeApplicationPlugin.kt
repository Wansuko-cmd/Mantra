package plugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import plugins.ext.alias
import plugins.ext.buildLogic
import plugins.ext.configureCommonAndroidSetting
import plugins.ext.getBundle
import plugins.ext.getLibrary
import plugins.ext.getPlugin
import plugins.ext.getVersion
import plugins.ext.implementation
import plugins.ext.libs
import plugins.ext.testImplementation

class AndroidComposeApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                alias(libs.getPlugin("android.application"))
                alias(libs.getPlugin("compose.compiler"))
            }

            extensions.configure<ApplicationExtension> {
                configureCommonAndroidSetting(this)
                defaultConfig {
                    targetSdk = buildLogic.getVersion("android.targetSdk").toInt()
                }
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                implementation(platform(libs.getLibrary("androidx.compose.bom")))
                implementation(libs.getBundle("androidx.compose"))
                testImplementation(libs.getBundle("androidx.compose.test"))
            }
        }
    }
}
