
import com.android.build.api.dsl.LibraryExtension
import com.pratik.convention.ExtensionType
import com.pratik.convention.configureBuildTypes
import com.pratik.convention.configureKotlinAndroid
import com.pratik.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("android-library").get().get().pluginId)
                apply(libs.findPlugin("jetbrainsKotlinAndroid").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(commonExtension = this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig{
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "testImplementation"(kotlin("test"))
            }
        }
    }
}