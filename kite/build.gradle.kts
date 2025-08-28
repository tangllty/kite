plugins {
    id("shared")
}

dependencies {
    api(libs.snakeyaml)
    api(kotlin("reflect"))
    api(libs.servlet)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
