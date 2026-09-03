# g2rain-department 文档

本目录是 `g2rain-department` 的项目级事实来源。项目计划接入 [`java-domain-service 1.0.0`](https://github.com/g2rain/g2rain/tree/architecture-v1.0.0/docs/architecture/profiles/java-domain-service)，当前仍需完成[架构差异](architecture/deviations.md)中的整改和复核。

## 项目定位

`g2rain-department` 维护租户内部门树、成员归属、IdP 部门映射和数据权限控制面，并向受信客户端提供主体增强和运行时策略解析。

## 文档导航

- 架构：[总览](architecture/overview.md) · [模块](architecture/modules.md) · [依赖](architecture/dependencies.md) · [运行流程](architecture/runtime-flows.md) · [差异](architecture/deviations.md)
- 设计：[IdP 部门同步](design/idp-department-sync.md) · [数据权限策略](design/data-permission-policy.md)
- 开发：[本地开发](development/local-development.md) · [代码规范](development/code-conventions.md) · [API](development/api-conventions.md) · [数据库](development/database-conventions.md) · [测试](development/testing.md) · [完成定义](development/definition-of-done.md)
- 治理：[需求模板](requirements/README.md) · [架构决策](decisions/README.md)
- 安全：[安全与租户边界](security/security-boundaries.md)
- 运维：[配置](operations/configuration.md) · [部署](operations/deployment.md) · [可观测性](operations/observability.md) · [故障排查](operations/troubleshooting.md)

README 用于项目介绍和快速上手；本目录维护可执行的工程约束。变更 API、数据库、配置、模块、依赖或长期设计时必须同步更新对应文档。
