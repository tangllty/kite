plugins {
    id("shared")
}

dependencies {
    api(libs.snakeyaml)
    api(kotlin("reflect"))
    api(libs.guava)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
