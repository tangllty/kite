plugins {
    id("shared")
}

dependencies {
    api(project(":jkorm"))
    implementation(libs.snakeyaml)
    implementation(libs.springContext)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
