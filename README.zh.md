# Kite

[**English**](./README.md) | **简体中文**

## 项目简介

Kite 是一个高效的轻量级 ORM 框架，基于 Kotlin 编写，开箱即用，内置分页查询、增删改查等常用功能，支持多表操作。它支持 PostgreSQL、MySQL、Derby 等多种数据库，旨在通过简化数据库操作，减少代码量，提升开发效率。

## 使用

 > Maven central: [kite-spring-boot-starter](https://central.sonatype.com/artifact/io.github.tangllty/kite-spring-boot-starter)

 1. 将以下依赖添加到您的项目中：

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

 2. 在 `application.properties` 文件中配置您的数据库连接信息
 3. 集成 `BaseMapper` 接口以创建一个 Mapper 接口

## 贡献

如果你有任何问题、建议或发现了 bug，请提交 [Issues](https://gitee.com/tangllty/kite/issues/new) 或提供 [Pull Request](https://gitee.com/tangllty/kite/pull/new) 来帮助改进该项目。

## 许可证

Kite 使用 MIT 许可证。更多详情请查阅 [LICENSE](https://gitee.com/tangllty/kite/blob/master/LICENSE) 文件。

## 交流群

通知：交流群目前已关闭，项目达到一定规模后将重新开放。
