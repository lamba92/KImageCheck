@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
    id("com.jfrog.bintray")
}

group = "com.github.lamba92"
version = `travis-tag` ?: "1.0"

repositories {
    jcenter()
    maven(url = "https://jitpack.io")
}

android {

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        minSdkVersion(14)
    }

    sourceSets.all {
        java.srcDirs(project.file("src/android${name.capitalize()}/kotlin"))
        res.srcDirs(project.file("src/android${name.capitalize()}/res"))
        resources.srcDirs(project.file("src/android${name.capitalize()}/resources"))
        manifest.srcFile(project.file("src/android${name.capitalize()}/AndroidManifest.xml"))
    }
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    android {
        publishLibraryVariants("release")
    }

    sourceSets {

        val resourceLoaderVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("com.github.lamba92:kresourceloader:$resourceLoaderVersion")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("com.github.lamba92:kresourceloader:$resourceLoaderVersion")
            }
        }
    }
}

bintray {
    user = searchPropertyOrNull("bintrayUsername", "BINTRAY_USERNAME")
    key = searchPropertyOrNull("bintrayApiKey", "BINTRAY_API_KEY")
    pkg {
        version {
            name = project.version as String
        }
        repo = "com.github.lamba92"
        name = "kimagecheck"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/lamba92/kimagecheck"
        issueTrackerUrl = "https://github.com/lamba92/kimagecheck/issues"
    }
    publish = true
    setPublications(*publishing.publications.names.toTypedArray(), "androidRelease")
}

tasks.withType<BintrayUploadTask> {
    doFirst {
        publishing.publications.withType<MavenPublication> {
            buildDir.resolve("publications/$name/module.json").let {
                if (it.exists())
                    artifact(object : org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(it) {
                        override fun getDefaultExtension() = "module"
                    })
            }
        }
    }
}

fun BintrayExtension.pkg(action: BintrayExtension.PackageConfig.() -> Unit) {
    pkg(closureOf(action))
}

fun BintrayExtension.PackageConfig.version(action: BintrayExtension.VersionConfig.() -> Unit) {
    version(closureOf(action))
}

fun searchPropertyOrNull(name: String, vararg aliases: String): String? {

    fun searchEverywhere(name: String): String? =
        findProperty(name) as? String? ?: System.getenv(name)

    searchEverywhere(name)?.let { return it }

    with(aliases.iterator()) {
        while (hasNext()) {
            searchEverywhere(next())?.let { return it }
        }
    }

    return null
}

@Suppress("PropertyName")
val `travis-tag`
    get() = System.getenv("TRAVIS_TAG").run {
        if (isNullOrBlank()) null else this
    }
