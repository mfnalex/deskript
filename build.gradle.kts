plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

group = "com.jeff-media"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}
tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.jeff_media.deskript.DeskriptKt"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = rootProject.name
            version = version

            from(components["kotlin"])
        }
    }
}