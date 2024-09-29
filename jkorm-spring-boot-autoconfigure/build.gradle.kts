plugins {
    id("shared")
}

dependencies {
    api(project(":jkorm"))
    api(project(":jkorm-spring"))
    implementation(libs.springBootStarter)
    implementation(libs.springBootStarterJdbc)
    implementation(libs.springBootAutoconfigure)
    annotationProcessor(libs.springBootAutoconfigureProcessor)
    annotationProcessor(libs.springBootConfigurationProcessor)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
    testImplementation(libs.springBootStarterTest)
}
