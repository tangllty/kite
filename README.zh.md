# jkorm

[**English**](./README.md) | **简体中文**

## 项目简介

JkOrm 是一个使用 Kotlin 编写的轻量级 ORM 框架，旨在简化数据库操作的开发。它支持多种数据库，如 PostgreSQL、MySQL、Derby 等，并提供了多种 API 来方便数据库操作的开发。

## 使用

> Maven central: [jkorm-spring-boot-starter](https://central.sonatype.com/artifact/io.github.tangllty/jkorm-spring-boot-starter)

1. 将以下依赖添加到您的项目中：

* Maven

```xml
<dependency>
   <groupId>io.github.tangllty</groupId>
   <artifactId>jkorm-spring-boot-starter</artifactId>
   <version>1.0.1</version>
</dependency>
```

* Gradle

```kts

implementation("io.github.tangllty:jkorm-spring-boot-starter:1.0.1")
```

2. 在 `application.properties` 文件中配置您的数据库连接信息
3. 集成 `BaseMapper` 接口以创建一个 Mapper 接口

## 贡献

如果你有任何问题、建议或发现了 bug，请提交 [Issues](https://gitee.com/tangllty/jkorm/issues/new) 或提供 [Pull Request](https://gitee.com/tangllty/jkorm/pull/new) 来帮助改进该项目。

## 许可证

jkorm 使用 MIT 许可证。更多详情请查阅 [LICENSE](https://gitee.com/tangllty/jkorm/blob/master/LICENSE) 文件。

## 交流群

- 微信

  - ![WeChat](https://gitee.com/tangllty/tang-docs/raw/master/docs/public/wechat.png)
- Telegram

  - ![Telegram](https://gitee.com/tangllty/tang-docs/raw/master/docs/public/telegram.png)
- QQ

  - ![QQ](https://gitee.com/tangllty/tang-docs/raw/master/docs/public/qq.png)
