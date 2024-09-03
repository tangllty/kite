plugins {
    kotlin("jvm")
    id("maven-publish")
}

group = "com.tang"
version = "1.0.0-beta7"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.slf4j)
    implementation(libs.logback)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

tasks.register<Jar>("javadocJar") {
    dependsOn(tasks["javadoc"])
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
//                username = System.getenv("SONATYPE_USERNAME")
//                password = System.getenv("SONATYPE_TOKEN")
                username = "UeHhSBdJ"
                password = "dJaS7nbrFfKNkaOTXw/wcZu1wLbm7KL5i0pqJAodSa0g"
            }
        }
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/tangllty/jkorm")
//            credentials {
//                username = System.getenv("PACKAGES_ACTOR")
//                password = System.getenv("PACKAGES_PASSWORD")
//            }
//        }
    }
}
