// dependency versions
object Versions {
    const val retrofit = "2.9.0"
    const val gsonConverter = "2.9.0"
    const val gson = "2.10"
    const val koin = "3.4.0"
    const val koinTest = "3.1.3"
    const val okhttp = "4.11.0"
    const val okhttpLoginInterceptor = "4.11.0"
    const val datastoreCore = "1.0.0"
    const val datastorePref = "1.1.0-beta02"
    const val coroutines = "1.7.1"
    const val mockWebserver = "4.9.0"
    const val coroutinesTest = "1.7.1"
    const val mockito = "5.7.0"
    const val mockitoKotlin = "5.2.1"
    const val coreTesting = "2.2.0"
    const val composeTesting = "1.7.0-alpha04"
    const val composeUiTooling = "1.7.0-alpha04"
    const val resourceIdling = "3.0.2"
    const val lifecycleCompose = "2.7.0"
    const val workManager = "2.9.0"
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    // id("com.google.devtools.ksp")

}

android {

    namespace = "com.sample.currencyconversion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sample.currencyconversion"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /** dev added dependencies */

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}")
    implementation("com.google.code.gson:gson:${Versions.gson}")

    // Koin
    implementation("io.insert-koin:koin-android:${Versions.koin}")
    testImplementation("io.insert-koin:koin-test:${Versions.koinTest}")

    // ok http
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLoginInterceptor}")

    // data store
    implementation("androidx.datastore:datastore-core:${Versions.datastoreCore}")
    implementation("androidx.datastore:datastore-preferences:${Versions.datastorePref}")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}") // Replace with the desired version

    // mockito and coroutine unit test
    implementation("com.squareup.okhttp3:mockwebserver:${Versions.mockWebserver}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}")
    testImplementation("org.mockito:mockito-core:${Versions.mockito}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}")
    testImplementation("androidx.arch.core:core-testing:${Versions.coreTesting}")

    // compose android test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeTesting}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeUiTooling}")

    // resources idling android test
    androidTestImplementation("com.android.support.test.espresso:espresso-idling-resource:${Versions.resourceIdling}")

    // compose runtime
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleCompose}")

    // work manager runtime
    implementation("androidx.work:work-runtime-ktx:${Versions.workManager}")

}
