# 构建与部署

```bash
mvn clean verify
mvn -pl g2rain-department-startup -am package
./build.sh <tag>
```

Startup 使用 Jib 构建 `g2rain/g2rain-department:<version>`，基础镜像为 `eclipse-temurin:25-jre`，主类为 `com.g2rain.department.Application`。

部署前检查：

- MySQL 不低于 8.0.13，数据库迁移已备份、验证并具备回滚方案。
- Nacos、Redis、数据源和 `g2rain-syncer` 绑定按环境隔离。
- Gateway 路由、服务身份和管理端权限配置完成。
- 所有凭证由部署系统注入。
- 应用默认端口 `8085` 与 Jib 当前声明的 `8080` 已统一或通过部署映射明确处理。
- `mvn clean verify` 通过，IdP 全量同步先在非生产环境验证。
