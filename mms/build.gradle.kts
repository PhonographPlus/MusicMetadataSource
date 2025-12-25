import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    alias(libs.plugins.androidGradlePluginLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    id("com.vanniktech.maven.publish") version "0.32.0"
    id("signing")
}


val libVersion = "0.1.2"

android {
    compileSdk = 35
    buildToolsVersion = "35.0.0"

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

val secretPropsFile = rootProject.file("secrets.properties")
var secrets = Properties()
if (secretPropsFile.exists()) {
    secretPropsFile.inputStream().use {
        secrets.load(it)
    }
}

mavenPublishing {
    // signAllPublications()
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    configure(AndroidSingleVariantLibrary(publishJavadocJar = false))
    coordinates("io.github.phonographplus", "music-metadata-source", libVersion)
    pom {
        name.set("Music Metadata Source")
        description.set("A library contains encapsulated APIs from LastFM and Musicbrainz to fetch Music Metadata or Music Tags.")
        url.set("https://github.com/PhonographPlus/MusicMetadataSource")

        developers {
            developers {
                developer {
                    id.set("phonographplus")
                    name.set("PhonographPlus")
                }
            }
        }

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

if (secretPropsFile.exists()) {
    signing {
        sign(publishing.publications)
        val key = File(secrets["signing_file"] as String).readText()
        useInMemoryPgpKeys(
            secrets["signing_key"] as String,
            key,
            secrets["signing_password"] as String
        )
    }
}