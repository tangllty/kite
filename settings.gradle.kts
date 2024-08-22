plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "jkorm"
include("jkorm")
include("jkorm-spring")
include("jkorm-spring-boot-autoconfigure")
include("jkorm-spring-boot-starter")
