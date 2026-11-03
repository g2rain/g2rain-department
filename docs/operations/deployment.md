# 构建与部署

```bash
mvn clean verify
mvn -pl g2rain-department-startup -am package
./build.sh 1.0.0
```

## 构建、镜像与 GHCR 发布

本地 Maven 构建与测试使用 `mvn clean verify`；它不会构建容器或推送镜像。`./build.sh <tag>` 使用同一 Startup Jib 配置构建到本机 Docker daemon，默认镜像名为 `ghcr.io/g2rain/g2rain-department:<tag>`，不使用 `latest`。

最终镜像由 Jib 从 `eclipse-temurin:25.0.4.1_1-jre` 生成，运行 Spring Boot 可执行 Jar，主类为 `com.g2rain.department.Application`，声明端口为 `8085`。OCI 元数据包括 source、Apache-2.0 license、title、version 和 description。

GitHub Actions 的 GHCR 工作流仅在推送稳定版 `vMAJOR.MINOR.PATCH` Git tag 时运行。它会验证 tag 已进入 `origin/main`、同版本发布说明存在、tag 与 Maven `project.version` 一致，再执行根目录的完整 `mvn --batch-mode --no-transfer-progress clean install` 并用 `GITHUB_TOKEN` 推送：

- `ghcr.io/g2rain/g2rain-department:<version>`
- `ghcr.io/g2rain/g2rain-department:sha-<完整提交 SHA>`

镜像 job 成功后，GHCR 工作流以 `needs` 调用 `.github/workflows/publish-maven.yml`。Maven 工作流也可通过 `workflow_dispatch` 手动运行；两条路径共享发布并发组，所选提交必须在 `main` 历史中。它只执行 `-pl g2rain-department-api -am clean deploy`，因此发布根父 POM 与 API JAR/sources，不会发布 Biz 或 Startup，并上传至 `https://maven.pkg.github.com/g2rain/g2rain-department`。两项发布不是事务：Maven 上传失败会清晰失败，但已成功推送的镜像不会回滚。

不创建 GitHub Release、不部署服务，也不更新 `latest`。CI 容器构建在 Startup 模块目录执行 Jib，复用本仓库 Jib 的基础镜像、入口、端口和 OCI 元数据；本地 `build.sh` 同样复用该配置，但仅将镜像写入本机 Docker daemon。匿名拉取仅在包设为公开后可用：

```bash
docker pull ghcr.io/g2rain/g2rain-department:1.0.0
```

首次成功推送后，在 GitHub Packages 的 **Package settings → Change visibility → Public** 将包设为公开；若组织策略禁止修改，请联系组织管理员。若已有同名 GHCR 包推送被拒绝，还须在 **Package settings → Actions access** 授予 `g2rain-department` 写权限，并检查组织 Packages 策略。`packages: write` 本身不能绕过这些设置。

GitHub Maven Packages 与公开 GHCR 镜像不同：Maven 消费方必须配置仓库和认证。例如，跨仓库的 GitHub Actions 消费需要为消费仓库授予包访问权限；个人开发者使用具备 `read:packages` 的 classic PAT，并通过本机或 CI 的 Maven `settings.xml` 安全注入，绝不提交 Token。重复上传相同稳定版本通常会冲突，工作流不删除或覆盖既有制品。

镜像包含 Jib 从 Startup 构建产物组装的依赖、resources 与应用 classes，不是仓库工作区的逐文件 Docker COPY；仓库没有 Dockerfile 或 `.dockerignore`。已跟踪的 `application-dev.yml` 和 `codegen.properties` 含开发凭据风险（见 DPT-004），当前 Jib 资源配置会将 `application-dev.yml` 打入可执行 Jar，进而进入镜像。投产必须通过环境变量、Secret 或 Nacos 注入并覆盖配置，维护者还应轮换任何已暴露的值；本次未删除文件或改写历史。

部署前检查：

- MySQL 不低于 8.0.13，数据库迁移已备份、验证并具备回滚方案。
- Nacos、Redis、数据源和 `g2rain-syncer` 绑定按环境隔离。
- Gateway 路由、服务身份和管理端权限配置完成。
- 所有凭证由部署系统注入。
- 应用默认端口与 Jib 声明端口均为 `8085`；如覆盖 `SERVER_PORT`，同步调整 Service 与健康检查。
- `mvn clean verify` 通过，IdP 全量同步先在非生产环境验证。
