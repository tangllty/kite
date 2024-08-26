plugins {
    id("shared")
}

dependencies {
    implementation(libs.snakeyaml)
    implementation(kotlin("reflect"))
    implementation(libs.guava)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
