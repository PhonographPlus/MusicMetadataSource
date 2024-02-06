plugins {
    alias(libs.plugins.androidGradlePluginLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    id("maven-publish")
}

android {
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        minSdk = 21
        namespace = "lib.mms"

        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        publishing {
            singleVariant("release") {
                withSourcesJar()
            }
        }
    }

    buildFeatures {
        buildConfig = false
        // viewBinding = true
        // compose = true
    }
}

dependencies {
    implementation(libs.androidx.annotation)

    implementation(libs.okhttp3)
    implementation(libs.retrofit2)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)

}


val libVersion = "0.0.1"
publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
            groupId = "io.github.chr56"
            artifactId = "music-metadata-source"
            version = libVersion

            pom {
                name.set("Music Metadata Source")
                url.set("https://github.com/PhonographPlus/MusicMetadataSource")

                licenses {
                    license {
                        name.set("MPL-2.0")
                        url.set("https://www.mozilla.org/MPL/2.0/")
                    }
                }
                scm {
                    connection.set("https://github.com/PhonographPlus/MusicMetadataSource.git")
                    developerConnection.set("https://github.com/PhonographPlus/MusicMetadataSource.git")
                    url.set("https://github.com/PhonographPlus/MusicMetadataSource")
                }
            }
        }
    }
}
