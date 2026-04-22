plugins {
    id("shared")
}

dependencies {
    api(project(":kite-core"))
    api(libs.springContext)
    api(libs.springJdbc)
    testImplementation(libs.h2)
}
