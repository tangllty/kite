plugins {
    id("shared")
}

dependencies {
    api(project(":kite-core"))
    api(project(":kite-spring"))
    api(libs.freemarker)
    testImplementation(libs.h2)
    testImplementation(kotlin("test"))
}
