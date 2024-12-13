plugins {
    id("shared")
}

dependencies {
    api(project(":jkorm"))
    api(libs.springContext)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
