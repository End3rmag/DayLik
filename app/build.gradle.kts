plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.0.21"



}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
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
        compose = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.benchmark.macro)
    dependencies {
        implementation ("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
        implementation ("com.squareup.okhttp3:okhttp:4.9.3")
        implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
        implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
        implementation("io.insert-koin:koin-androidx-compose:4.1.0-Beta5")
        implementation ("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

// Room для хранения задач
        implementation ("androidx.room:room-runtime:2.4.2")
        implementation ("androidx.room:room-ktx:2.4.2")
        implementation ("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.datastore:datastore-preferences:1.1.3")

        implementation("androidx.navigation:navigation-compose:2.8.9")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")

        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
        implementation(platform("androidx.compose:compose-bom:2025.01.01"))
        implementation("androidx.compose.material3:material3")
        implementation ("androidx.compose.material:material-icons-extended")
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.animation.core.lint)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    }}