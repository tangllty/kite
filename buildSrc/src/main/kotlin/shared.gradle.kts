plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

group = "io.github.tangllty"
version = "1.0.10"

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
                description.set("Kite is a lightweight ORM framework for Kotlin and Java.")
                url.set("https://github.com/tangllty/kite")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("tang")
                        name.set("tang")
                        email.set("tanglly@163.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/tangllty/kite.git")
                    developerConnection.set("scm:git:ssh://github.com/tangllty/kite.git")
                    url.set("http://github.com/tangllty/kite")
                }
            }
        }
    }
    repositories {
        maven {
            name = "Project"
            url = uri(layout.buildDirectory.dir("repositories"))
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_SECRET_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(publishing.publications["mavenJava"])
}

tasks.register<SonatypeCentralPublishTask>("publishToSonatypeCentral") {
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_TOKEN")
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
