plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.android.room)

}

android {
    namespace = "com.pratik.core.database"
}

dependencies {
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
}