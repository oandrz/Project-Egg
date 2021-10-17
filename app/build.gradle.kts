import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 23 - 8 - 2020.
 */

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = keystoreProperties["storeFile"]?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    defaultConfig {
        applicationId = "starbright.com.projectegg"
        minSdk = 21
        targetSdk = 30
        versionCode = 5
        versionName = "0.1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        buildConfigField("String", "CLARIFAI_KEY", "\"d724362606ac43c39a3a3cee03b85657\"")
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("String", "BASE_URL", "\"https://5y482.mocklab.io\"")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = false
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://api.spoonacular.com\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    flavorDimensions("version")
    productFlavors {
        create("develop") {
            dimension = "version"
            versionNameSuffix = "-develop"
            resValue("string", "app_name", "\"Chefnut-Dev\"")
            buildConfigField("String", "SPOON_KEY", "\"c14f35c6748443ef89c645e7a09404c7\"")
        }

        create("production") {
            dimension = "version"
            versionNameSuffix = "-full"
            resValue("string", "app_name", "\"Chefnut\"")
            buildConfigField("String", "SPOON_KEY", "\"c14f35c6748443ef89c645e7a09404c7\"")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //Kotlin
    val kotlinVersion: String by rootProject.extra
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")

    //android support & recyclerview
    val legacySupportVersion: String by rootProject.extra
    implementation("androidx.appcompat:appcompat:$legacySupportVersion")

    val materialDesignVersion: String by rootProject.extra
    implementation("com.google.android.material:material:$materialDesignVersion")

    //dagger
    val daggerVersion: String by rootProject.extra
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    implementation("androidx.constraintlayout:constraintlayout:2.1.1")

    //Retrofit
    val retrofitVersion: String by rootProject.extra
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")

    //Android RX
    val rxjavaVersion: String by rootProject.extra
    val rxAndroidVersion: String by rootProject.extra
    implementation("io.reactivex.rxjava2:rxjava:$rxjavaVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxAndroidVersion")

    //OkHttp
    val okhttpVersion: String by rootProject.extra
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    //Clarifai
    val clarifaiVersion: String by rootProject.extra
    implementation("com.clarifai.clarifai-api2:core:$clarifaiVersion")

    //HotChemi
    val hotchemiVersion: String by rootProject.extra
    implementation("com.github.permissions-dispatcher:permissionsdispatcher:$hotchemiVersion")
    kapt("com.github.permissions-dispatcher:permissionsdispatcher-processor:$hotchemiVersion")

    //Glide
    val glideVersion: String by rootProject.extra
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    //Firebase
    val firebaseCoreVersion: String by rootProject.extra
    implementation("com.google.firebase:firebase-core:$firebaseCoreVersion")
    implementation("com.google.firebase:firebase-analytics:$firebaseCoreVersion")
    val firebaseAuthVersion: String by rootProject.extra
    implementation("com.google.firebase:firebase-auth:$firebaseAuthVersion")
    val firebaseCloudStoreVersion: String by rootProject.extra
    implementation("com.google.firebase:firebase-firestore-ktx:$firebaseCloudStoreVersion")

    //endless Recycler View
    val fastAdapterVersion: String by rootProject.extra
    implementation("com.mikepenz:fastadapter:$fastAdapterVersion")
    implementation("com.mikepenz:fastadapter-extensions-scroll:$fastAdapterVersion") // scroll helpers
    implementation("com.mikepenz:fastadapter-extensions-ui:$fastAdapterVersion")

    //Material Dialog
    val materialDialogVersion: String by rootProject.extra
    implementation("com.afollestad.material-dialogs:core:$materialDialogVersion")

    //Circle Image View
    val circleIVVersion: String by rootProject.extra
    implementation("de.hdodenhof:circleimageview:$circleIVVersion")

    //Zeibatsu Compressor
    val zeibatsuVersion: String by rootProject.extra
    implementation("id.zelory:compressor:$zeibatsuVersion")

    //Preference
    val preferenceVersion: String by rootProject.extra
    implementation("androidx.preference:preference:$preferenceVersion")

    //Room
    val roomVersion: String by rootProject.extra
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Unit Test
    val junitVersion: String by rootProject.extra
    testImplementation("junit:junit:$junitVersion")
    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    kaptTest("com.google.dagger:dagger-compiler:$daggerVersion")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    val hamcrestVersion: String by rootProject
    testImplementation("org.hamcrest:hamcrest-library:$hamcrestVersion")
}
