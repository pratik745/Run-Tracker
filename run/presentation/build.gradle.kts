plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.pratik.run.presentation"
}

dependencies {

    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)
    implementation(libs.core.ktx)
    testImplementation(libs.junit.junit)

    implementation(libs.bundles.koin)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}