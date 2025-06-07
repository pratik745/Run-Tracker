plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "com.pratik.run.network"
}

dependencies {
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.bundles.koin)
}