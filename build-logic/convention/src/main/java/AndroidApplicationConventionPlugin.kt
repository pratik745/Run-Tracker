import com.android.build.api.dsl.ApplicationExtension
import com.pratik.convention.ExtensionType
import com.pratik.convention.configureBuildTypes
import com.pratik.convention.configureKotlinAndroid
import com.pratik.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("android-application").get().get().pluginId)
                apply(libs.findPlugin("jetbrainsKotlinAndroid").get().get().pluginId)
            }

            extensions.configure<ApplicationExtension>{
                defaultConfig{
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                }

                configureKotlinAndroid(commonExtension = this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}