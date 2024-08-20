plugins {
    id("shared")
}

dependencies {
    implementation(libs.snakeyaml)
    implementation(libs.derby)
    implementation(libs.derbytools)
    implementation(kotlin("reflect"))
}
