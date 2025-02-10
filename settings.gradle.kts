plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "kite"
include("kite")
include("kite-spring")
include("kite-spring-boot-autoconfigure")
include("kite-spring-boot-starter")
