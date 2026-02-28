plugins {
    id("shared")
    kotlin("kapt")
}

dependencies {
    api(project(":kite"))
    api(project(":kite-spring"))
    kapt(libs.springBootConfigurationProcessor)
    implementation(libs.springBootStarter)
    implementation(libs.springBootStarterJdbc)
    implementation(libs.springBootAutoconfigure)
    annotationProcessor(libs.springBootAutoconfigureProcessor)
    annotationProcessor(libs.springBootConfigurationProcessor)
    testImplementation(libs.h2)
    testImplementation(libs.springBootStarterTest)
}

kapt {
    useBuildCache = false
    showProcessorStats = true
}

tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}
