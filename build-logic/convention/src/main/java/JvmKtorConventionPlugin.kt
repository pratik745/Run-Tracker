import com.pratik.convention.configureKotlinJvm
import com.pratik.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmKtorConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run{
                apply(libs.findPlugin("kotlin-serialization").get().get().pluginId)
            }
            dependencies {
                "implementation"(libs.findBundle("ktor").get())
            }
        }
    }
}