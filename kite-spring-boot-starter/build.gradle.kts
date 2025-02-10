plugins {
    id("shared")
}

dependencies {
    api(project(":kite"))
    api(project(":kite-spring"))
    api(project(":kite-spring-boot-autoconfigure"))
}
