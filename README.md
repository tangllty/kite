# Kite

**English** | [**简体中文**](./README.zh.md)

## Project Description

Kite is a high-performance, lightweight ORM framework written in Kotlin. It is designed to be ready to use out of the box, with built-in features for pagination, CRUD operations, and support for multi-table operations. Kite supports multiple databases such as PostgreSQL, MySQL, Derby, etc., and aims to simplify database operations, reduce code complexity, and enhance development efficiency.

## Usage

 > Maven central: [kite-spring-boot-starter](https://central.sonatype.com/artifact/io.github.tangllty/kite-spring-boot-starter)

 1. Add the following dependencies to your project:

 * Maven

```xml
<dependency>
   <groupId>io.github.tangllty</groupId>
   <artifactId>kite-spring-boot-starter</artifactId>
   <version>1.0.11</version>
</dependency>
```

 * Gradle

```kts
implementation("io.github.tangllty:kite-spring-boot-starter:1.0.11")
```

 2. Configure your database connection information in the `application.properties` file

 3. Extend the `BaseMapper` interface to create a Mapper interface

## Contributing

If you have any questions, suggestions, or find bugs, please submit an [Issues](https://github.com/tangllty/kite/issues/new) or provide a [Pull Request](https://github.com/tangllty/kite/pull/new) to help improve the project.

## License

Kite uses the MIT license. For more details, please refer to [LICENSE](https://github.com/tangllty/kite/blob/master/LICENSE) files.

## Discussion Group

Notice: The discussion group is currently closed, and the group will be reopened when the project reaches a certain scale.
