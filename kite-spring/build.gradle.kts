plugins {
    id("shared")
}

dependencies {
    api(project(":kite"))
    api(libs.springContext)
    api(libs.springJdbc)
    testImplementation(libs.h2)
}
