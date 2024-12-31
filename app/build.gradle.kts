plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "tn.esprit.car_sell"
    compileSdk = 34

    defaultConfig {
        applicationId = "tn.esprit.car_sell"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)  // Make sure this is defined in your version catalog
    implementation(libs.material)  // Make sure this is defined in your version catalog
    testImplementation(libs.junit) // Ensure JUnit dependency is available
    androidTestImplementation(libs.ext.junit) // For Android specific JUnit support
    androidTestImplementation(libs.espresso.core) // For UI testing
    implementation("com.google.android.material:material:1.7.0")


}