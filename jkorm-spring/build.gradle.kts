plugins {
    id("shared")
}

dependencies {
    implementation(project(":jkorm"))
    implementation(libs.snakeyaml)
    implementation(libs.springContext)
    implementation(libs.slf4j)
    testImplementation(libs.logback)
}
