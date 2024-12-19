plugins {
    id("shared")
}

dependencies {
    api(project(":jkorm"))
    api(libs.springContext)
    api(libs.springJdbc)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
