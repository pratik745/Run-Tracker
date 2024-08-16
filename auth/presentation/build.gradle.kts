plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.pratik.auth.presentation"
}

dependencies {
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}