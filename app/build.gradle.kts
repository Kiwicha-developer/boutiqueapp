plugins {
    //alias(libs.plugins.android.application)
    //alias(libs.plugins.kotlin.android)
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.cibertec.boutiquesmart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cibertec.boutiquesmart"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation("androidx.activity:activity-ktx:1.8.2")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("androidx.media:media:1.6.0")
    implementation ("com.flaviofaria:kenburnsview:1.0.7")
    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")


    configurations.all{
        resolutionStrategy{
            force("androidx.activity:activity:1.8.2")
            force("androidx.activity:activity-ktx:1.8.2")
            force("androidx.fragment:fragment:1.6.2")
            force("androidx.fragment:fragment-ktx:1.6.2")
        }
    }
}