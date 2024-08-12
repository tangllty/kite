/**
 * Dependencies versions
 *
 * @author Tang
 */
class Versions {
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml
    val snakeyaml = "2.2"
    // https://mvnrepository.com/artifact/org.apache.derby/derby
    val derby = "10.16.1.1"
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    val slf4j = "2.0.13"
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    val logback = "1.5.6"
    // https://mvnrepository.com/artifact/org.springframework/spring-core/6.1.11
    val spring = "6.1.11"
}

class Libraries {
    val snakeyaml = "org.yaml:snakeyaml:${versions.snakeyaml}"
    val derby = "org.apache.derby:derby:${versions.derby}"
    val derbytools = "org.apache.derby:derbytools:${versions.derby}"
    val slf4j = "org.slf4j:slf4j-api:${versions.slf4j}"
    val logback = "ch.qos.logback:logback-classic:${versions.logback}"
    val spring = "org.springframework:spring-core:${versions.spring}"
}

val versions = Versions()
val libs = Libraries()
