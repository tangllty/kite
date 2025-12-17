plugins {
    id("shared")
}

dependencies {
    api(project(":kite"))
    api(project(":kite-spring")) {
        exclude(group = "org.springframework", module = "spring-context")
        exclude(group = "org.springframework", module = "spring-jdbc")
    }
    api(libs.springContext6)
    api(libs.springJdbc6)
    testImplementation(libs.h2)
}
