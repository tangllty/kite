plugins {
    id("shared")
}

dependencies {
    implementation(libs.snakeyaml)
    implementation(kotlin("reflect"))
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
