plugins {
    id("shared")
}

dependencies {
    api(project(":kite"))
    api(libs.freemarker)
    testImplementation(libs.h2)
    testImplementation(kotlin("test"))
}
