/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 17 - 8 - 2020.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    extra.apply {
        set("kotlinVersion", "1.5.31")
        set("materialDesignVersion", "1.3.0-alpha02")
        set("cardViewVersion", "1.0.0")
        set("legacySupportVersion", "1.2.0")
        set("jUnitVersion", "4.12")
        set("clarifaiVersion", "2.4.0")
        set("daggerVersion", "2.28")
        set("retrofitVersion", "2.9.0")
        set("okhttpVersion", "4.7.2")
        set("rxjavaVersion", "2.2.19")
        set("rxAndroidVersion", "2.1.1")
        set("hotchemiVersion", "4.9.1")
        set("glideVersion", "4.11.0")
        set("firebaseCoreVersion", "17.4.2")
        set("firebaseAuthVersion", "19.3.1")
        set("firebaseCloudStoreVersion", "21.4.3")
        set("firebaseAnalytic", "17.4.2")
        set("materialDialogVersion", "3.3.0")
        set("circleIVVersion", "2.2.0")
        set("roomVersion", "2.2.5")
        set("zeibatsuVersion", "3.0.1")
        set("adMobVersion", "19.1.0")
        set("preferenceVersion", "1.1.1")
        set("fastAdapterVersion", "5.0.2")
        set("junitVersion", "4.12")
        set("espressoVersion", "3.1.0")
        set("mockitoVersion", "2.7.1")
        set("coreTestingVersion", "1.1.1")
        set("testRunnerVersion", "1.1.1")
        set("testExtRunnerVersion", "1.1.0")
        set("hamcrestVersion", "2.1")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        val kotlinVersion: String by rootProject.extra
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.jakewharton.hugo:hugo-plugin:1.2.1")
        classpath("com.google.gms:google-services:4.3.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
