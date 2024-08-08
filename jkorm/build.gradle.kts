plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.tang"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.snakeyaml)
    implementation(libs.derby)
    implementation(libs.derbytools)
    implementation(libs.slf4j)
    testImplementation(libs.logback)
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
