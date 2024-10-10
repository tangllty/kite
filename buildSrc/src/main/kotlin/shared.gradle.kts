plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

group = "io.github.tangllty"
version = "1.0.0-beta9"

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
            pom {
                name.set(project.name)
                description.set("A simple ORM framework written in Kotlin")
                url.set("https://github.com/tangllty/jkorm")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("tangllty")
                        name.set("tangllty")
                        email.set("tanglly@163.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/tangllty/jkorm.git")
                    developerConnection.set("scm:git:ssh://github.com/tangllty/jkorm.git")
                    url.set("http://github.com/tangllty/jkorm")
                }
            }
        }
    }
    repositories {
        maven {
            name = "Project"
            url = uri(layout.buildDirectory.dir("repositories"))
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/tangllty/jkorm")
            credentials {
                username = System.getenv("PACKAGES_ACTOR")
                password = System.getenv("PACKAGES_PASSWORD")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_SECRET_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(publishing.publications["mavenJava"])
}

tasks.register<SonatypeCentralPublishTask>("publishToSonatypeCentral") {
    username = "xxxxxxxx"
    password = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
