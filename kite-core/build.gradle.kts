plugins {
    id("shared")
}

dependencies {
    api(libs.snakeyaml)
    api(kotlin("reflect"))
    testImplementation(libs.h2)
}
