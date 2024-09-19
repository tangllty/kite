plugins {
    id("shared")
}

dependencies {
    api(project(":jkorm"))
    api(libs.springContext)
    api(libs.springWeb)
    testImplementation(libs.derby)
    testImplementation(libs.derbytools)
}
