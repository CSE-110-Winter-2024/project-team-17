plugins {
    id("com.android.application")
}

android {
    namespace = "edu.ucsd.cse110.successorator"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.ucsd.cse110.successorator"
        minSdk = 31
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    project(":lib")
    implementation(project(":lib"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("net.bytebuddy:byte-buddy:1.11.22") // Ensure this version is compatible with your Mockito version
    testImplementation("org.mockito:mockito-core:3.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
