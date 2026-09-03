# 本地开发

## 环境

- JDK 25
- Maven 3.9+
- MySQL 8.0.13+
- Redis
- Nacos（仅在启用相应配置时必需）

## 初始化与运行

初始化数据库前检查脚本内容；`scripts/g2rain-department.sql` 是建表脚本，不应未经评估直接用于已有数据环境。

```bash
mvn clean verify
mvn -pl g2rain-department-startup -am spring-boot:run
```

默认 profile 为 `dev`，HTTP 端口为 `8085`。本地数据库和基础设施连接应通过环境变量、私有配置或 Nacos 覆盖，不要提交真实凭证。

常用检查：

```bash
mvn -pl g2rain-department-biz -am test
mvn -pl g2rain-department-startup -am package
```
