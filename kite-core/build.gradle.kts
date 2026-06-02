plugins {
    id("shared")
}

dependencies {
    api(libs.snakeyaml)
    api(kotlin("reflect"))
    compileOnly(libs.servlet)
    testImplementation(libs.h2)
}
