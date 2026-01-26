/**
 * Dependencies versions
 *
 * @author Tang
 */
class Versions {
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml
    val snakeyaml = "2.5"
    // https://mvnrepository.com/artifact/com.h2database/h2
    val h2 = "2.4.240"
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    val slf4j = "2.0.17"
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    val logback = "1.5.26"
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    val servlet = "6.1.0"
    // https://mvnrepository.com/artifact/org.freemarker/freemarker
    val freemarker = "2.3.34"
    // https://mvnrepository.com/artifact/org.springframework/spring-core
    val spring6 = "6.2.15"
    // https://mvnrepository.com/artifact/org.springframework/spring-core
    val spring = "7.0.3"
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot
    val springBoot3 = "3.5.10"
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot
    val springBoot = "4.0.2"
}

class Libraries {
    val snakeyaml = "org.yaml:snakeyaml:${versions.snakeyaml}"
    val h2 = "com.h2database:h2:${versions.h2}"
    val slf4j = "org.slf4j:slf4j-api:${versions.slf4j}"
    val logback = "ch.qos.logback:logback-classic:${versions.logback}"
    val servlet = "jakarta.servlet:jakarta.servlet-api:${versions.servlet}"
    val freemarker = "org.freemarker:freemarker:${versions.freemarker}"
    val springContext6 = "org.springframework:spring-context:${versions.spring6}"
    val springJdbc6 = "org.springframework:spring-jdbc:${versions.spring6}"
    val springContext = "org.springframework:spring-context:${versions.spring}"
    val springJdbc = "org.springframework:spring-jdbc:${versions.spring}"
    val springBootStarter3 = "org.springframework.boot:spring-boot-starter:${versions.springBoot3}"
    val springBootStarterJdbc3 = "org.springframework.boot:spring-boot-starter-jdbc:${versions.springBoot3}"
    val springBootAutoconfigure3 = "org.springframework.boot:spring-boot-autoconfigure:${versions.springBoot3}"
    val springBootAutoconfigureProcessor3 = "org.springframework.boot:spring-boot-autoconfigure-processor:${versions.springBoot3}"
    val springBootConfigurationProcessor3 = "org.springframework.boot:spring-boot-configuration-processor:${versions.springBoot3}"
    val springBootStarterTest3 = "org.springframework.boot:spring-boot-starter-test:${versions.springBoot3}"
    val springBootStarter = "org.springframework.boot:spring-boot-starter:${versions.springBoot}"
    val springBootStarterJdbc = "org.springframework.boot:spring-boot-starter-jdbc:${versions.springBoot}"
    val springBootAutoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${versions.springBoot}"
    val springBootAutoconfigureProcessor = "org.springframework.boot:spring-boot-autoconfigure-processor:${versions.springBoot}"
    val springBootConfigurationProcessor = "org.springframework.boot:spring-boot-configuration-processor:${versions.springBoot}"
    val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${versions.springBoot}"
}

val versions = Versions()
val libs = Libraries()
