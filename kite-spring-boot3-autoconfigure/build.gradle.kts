plugins {
    id("shared")
    kotlin("kapt")
}

dependencies {
    api(project(":kite"))
    api(project(":kite-spring6"))
    api(project(":kite-spring-boot-autoconfigure")) {
        exclude(group = "org.springframework.boot", module = "spring-boot-autoconfigure")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-jdbc")
    }
    kapt(libs.springBootConfigurationProcessor3)
    implementation(libs.springBootStarter3)
    implementation(libs.springBootStarterJdbc3)
    implementation(libs.springBootAutoconfigure3)
    annotationProcessor(libs.springBootAutoconfigureProcessor3)
    annotationProcessor(libs.springBootConfigurationProcessor3)
    testImplementation(libs.h2)
    testImplementation(libs.springBootStarterTest3)
}

kapt {
    useBuildCache = false
    showProcessorStats = true
}

tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}
