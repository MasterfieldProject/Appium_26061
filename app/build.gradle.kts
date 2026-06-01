plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "hu.masterfield.appium"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "hu.masterfield.appium"
        minSdk = 35
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation("io.appium:java-client:10.1.1")
    androidTestImplementation("org.seleniumhq.selenium:selenium-java:4.44.0")
    androidTestImplementation("org.testng:testng:7.12.0")
    androidTestImplementation("commons-io:commons-io:2.22.0")
    androidTestImplementation("org.apache.commons:commons-lang3:3.20.0")
}