plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.pratik.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}