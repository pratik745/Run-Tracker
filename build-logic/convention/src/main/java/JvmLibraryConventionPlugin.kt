import com.pratik.convention.configureKotlinJvm
import com.pratik.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run{
                apply(libs.findPlugin("org-jetbrains-kotlin-jvm").get().get().pluginId)
            }
            configureKotlinJvm()
        }
    }
}