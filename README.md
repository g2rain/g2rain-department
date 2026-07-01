# g2rain-department

## 1. 徽标与状态标识

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-25-437291?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.1-586069?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)

## 2. 项目简介

`g2rain-department` 是 G2rain 平台中的部门与数据权限后端服务，围绕组织部门树、人员归属和数据权限策略提供统一领域能力，用于支撑平台可选增强的组织治理与数据权限控制场景。

## 3. 平台定位

在 G2rain“企业级 AI 原生开源 SaaS 平台”体系中，`g2rain-department` 位于平台能力增强层，是部门与数据权限相关平台能力的独立服务实现。

它主要服务以下场景：
- 为业务系统提供部门树、部门成员关系与组织路径计算能力
- 为数据隔离与数据权限场景提供模型、元数据、权限组、权限规则与用户绑定能力
- 为平台权限治理提供可选增强组件，而不是强绑定所有业务域的底座服务
- 为权限变化后的缓存失效、策略广播和运行时刷新提供统一后端实现

它与 `g2rain-basis`、`g2rain-iam`、`g2rain-infra` 及上层业务域服务协同，形成完整的平台组织治理与数据权限增强体系。

## 4. 核心能力

本章回答“这个仓库在平台里提供什么能力、解决什么问题”。

- 组织部门树管理能力：解决企业组织结构层级化维护的问题，通过 `Department` 与 `DepartmentUserRelation` 提供部门树、用户归属和组织路径管理能力。
- 数据权限模型管理能力：解决不同业务模块、不同表如何接入统一权限治理的问题，通过 `DataPermissionModel`、`DataPermissionField`、`DataPermissionMeta` 定义权限模型、字段和元数据。
- 数据权限分组与分配能力：解决权限策略如何落到具体用户的问题，通过 `DataPermissionGroup`、`DataPermissionGroupUserRelation`、`DataPermissionOther` 完成分组、绑定和特殊规则配置。
- 权限策略解析能力：解决运行时如何把组织、用户、模块、表名解析为可执行的数据权限策略的问题，通过 `resolveDataPermissionPolicy` 提供统一解析入口。
- 权限变更广播能力：解决权限数据更新后运行时缓存如何同步失效的问题，通过 `DataPermissionPolicyCacheBroadcaster`、同步通道与策略变化探测器广播变更。
- 标准化服务交付能力：解决增强组件的独立部署与镜像交付问题，通过 `build.sh` 与 `Jib` 提供统一的镜像构建入口。

## 5. 技术栈

- 语言与运行时：`Java 25`
- 后端框架：`Spring Boot 4.0.5`、`Spring Cloud 2025.1.1`
- 配置与注册：`Nacos Discovery`、`Nacos Config`
- 持久化：`MyBatis Spring Boot Starter 4.0.1`
- 对象转换：`MapStruct 1.6.3`
- 平台基础依赖：`g2rain-common`、`g2rain-starter-aegis-core`、`g2rain-starter-cache-sync`、`g2rain-starter-stream-redis`、`g2rain-starter-mybatis-extensions`、`g2rain-starter-spring-doc`
- 构建与交付：`Maven`、`Jib`、`build.sh`

## 6. 快速开始

### 环境要求

- `JDK 25`
- `Maven 3.9+`
- 可用的 `Nacos`
- 可用的 Redis / 消息同步环境
- 可用的数据库与相关平台依赖环境

### 关键配置

当前仓库的关键运行配置主要来自 `g2rain-department-startup/src/main/resources/application.yml` 与 Nacos 配置中心。

| 变量名 | 说明 | 典型用途 |
| --- | --- | --- |
| `SERVER_PORT` | 服务端口 | 默认 `8085` |
| `SPRING_PROFILES_ACTIVE` | 启动环境 | 默认 `dev` |
| `NACOS_SERVER_ADDR` | Nacos 地址 | 服务注册与配置拉取 |
| `SPRING_CLOUD_NACOS_DISCOVERY_NAMESPACE` | 注册命名空间 | 环境隔离 |
| `SPRING_CLOUD_NACOS_CONFIG_NAMESPACE` | 配置命名空间 | 环境隔离 |
| `spring.cloud.stream.*` | 同步消息通道配置 | 权限策略缓存广播 |

### 本地构建

```bash
mvn clean package -DskipTests
```

### 本地运行

```bash
mvn -pl g2rain-department-startup spring-boot:run
```

### 镜像构建

```bash
./build.sh
./build.sh 1.0.0
```

或：

```bash
cd g2rain-department-startup
mvn -DskipTests=true compile jib:dockerBuild -Djib.to.image=g2rain/g2rain-department:latest
```

## 7. 项目结构

本章回答“代码与模块是如何组织的、排查和扩展时应该先看哪里”。

```text
g2rain-department/
├── build.sh
├── codegen.properties
├── pom.xml
├── g2rain-department-api/
├── g2rain-department-biz/
│   ├── controller
│   ├── service
│   ├── service/support
│   ├── dao
│   ├── converter
│   └── resources/mybatis/mapper
└── g2rain-department-startup/
    └── src/main/resources/
```

### 结构说明

- `g2rain-department-api`：对外暴露组织与数据权限接口契约。
- `g2rain-department-biz/controller`：承载部门、部门成员、权限模型、权限元数据、权限组等 HTTP 入口。
- `g2rain-department-biz/service/impl`：承载组织树、权限策略、广播逻辑调用等核心业务实现。
- `g2rain-department-biz/service/support`：承载状态更新、权限变化探测、缓存广播等支撑逻辑。
- `g2rain-department-biz/dao` 与 `resources/mybatis/mapper`：承载持久化接口与 SQL 映射。
- `g2rain-department-startup`：承载服务启动配置、Profile 配置、日志与 Nacos 接入。
- `build.sh`：仓库默认镜像交付入口。

## 8. 核心业务流程

本章回答“这些能力在运行时是如何串起来工作的”。

#### 1. 部门树维护主线

- 客户端通过 `DepartmentController` 提交部门查询或保存请求。
- `DepartmentServiceImpl` 在新增时根据父节点和同级最大 `deptPath` 计算新的部门路径。
- 更新时会校验父子关系，避免把节点移动到自己的子树中。
- 最终把部门层级结构持久化，并在需要时返回树形结果。

#### 2. 用户归属与组织路径主线

- 客户端维护 `DepartmentUserRelation` 以绑定用户和部门。
- 运行时可按 `organId + userId` 反查用户所属部门路径集合。
- 这些路径会成为数据权限解析与缓存广播的重要输入。

#### 3. 数据权限模型解析主线

- 平台先通过 `DataPermissionModel` 定义模块与表。
- 再通过 `DataPermissionMeta`、`DataPermissionField`、`DataPermissionGroup`、`DataPermissionOther` 配置具体权限规则。
- 运行时调用 `resolveDataPermissionPolicy`，传入 `organId`、`userId`、`deptPaths`、`moduleCode`、`tableName`。
- `DataPermissionMetaServiceImpl` 归一化路径集合后，交由 DAO 层解析出最终策略结果。

#### 4. 权限变更广播主线

- 当权限元数据、特殊规则、用户分组关系、部门领导或用户部门归属发生变化时，服务层会调用 `DataPermissionPolicyCacheBroadcaster`。
- 广播器会根据变化对象推导受影响的 `organ`、`user`、`module`、`table` 范围。
- 然后通过 `EventPublisherHub` 发送到同步通道，通知下游刷新缓存。
- 这条主线解决的是“权限改了，运行时如何及时生效”的问题。

#### 5. 平台接入与交付主线

- 服务启动时由 `g2rain-department-startup` 读取本地配置与 Nacos 配置。
- 同时初始化 `spring.cloud.stream` 同步通道，接入权限广播链路。
- 交付阶段通过根目录 `build.sh` 先执行全仓 `mvn clean install`，再进入 `g2rain-department-startup` 使用 `jib:dockerBuild` 输出镜像。

## 9. 常用命令

```bash
mvn clean package
mvn -pl g2rain-department-startup spring-boot:run
mvn test
./build.sh
./build.sh 1.0.0
```

## 10. 质量与测试

- 当前仓库代码结构清晰，领域边界明确，适合作为平台能力增强服务长期演进。
- 当前扫描重点集中在主源码结构，后续建议优先补齐部门树迁移、权限策略解析、缓存广播等关键场景测试。
- 涉及消息广播和数据权限联动时，建议在联调环境验证实际缓存刷新与下游生效情况。

## 11. 相关仓库

- `g2rain-basis`：平台应用、角色、权限与组织治理底座
- `g2rain-infra`：平台基础设施能力协同
- `g2rain-iam`：统一身份认证服务
- `g2rain-department-app`：部门与数据权限前端子应用

## 12. 使用建议

- 适合作为平台可选增强组件独立部署，而不是直接嵌入到所有业务服务中。
- 适合与平台数据隔离、权限治理体系协同，向上提供统一的数据权限解析能力。
- 当新增业务模块接入数据权限时，建议先定义 `moduleCode + tableName` 再接入元数据与策略规则。

## 13. 贡献指南

欢迎通过文档改进、Issue 反馈、测试补充、代码优化、功能增强等形式参与贡献。

建议流程：
1. Fork 本仓库
2. 创建特性分支
3. 提交修改
4. 推送分支
5. 提交 Pull Request

提交前请尽量确保：
- 遵循现有技术栈与代码规范
- 补充必要测试
- 更新相关文档
- 确保测试通过

## 14. 许可证

本项目基于 [Apache 2.0许可证](LICENSE) 开源。

## 15. 联系我们

- **站点**: https://www.g2rain.com/
- **Issues**: [GitHub Issues](https://github.com/g2rain/g2rain/issues)
- **讨论**: [GitHub Discussions](https://github.com/g2rain/g2rain/discussions)
- **邮箱**: g2rain_developer@163.com

## 16. 致谢

感谢所有为这个项目做出贡献的开发者们。

如果这个项目对您有帮助，欢迎 Star 支持。
