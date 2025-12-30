plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "kite"
include("kite")
include("kite-spring")
include("kite-spring-boot-autoconfigure")
include("kite-spring-boot-starter")
include("kite-spring6")
include("kite-spring-boot3-autoconfigure")
include("kite-spring-boot3-starter")
