import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    id("maven-publish")
}

group = "it.lamba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.github.lamba92","kresourceloader","1.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets.getAt("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>(name) {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}