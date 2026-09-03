# g2rain-department Agent Instructions

本文件是 AI Coding 在本项目中的执行入口。项目事实来源位于 `docs`，组织级公共规则位于中央 `g2rain` 仓库。

## 架构基线

- Profile：`java-domain-service 1.0.0`
- 固定引用：`architecture-v1.0.0`
- 中央仓库：`https://github.com/g2rain/g2rain`
- 当前状态：`planned`，完成 `docs/architecture/deviations.md` 中的接入项后才能改为 `adopted`

## 开始工作前

1. 读取 `docs/project.yaml`、中央 Profile 和 `docs/architecture/deviations.md`。
2. 按任务读取架构、需求、设计、开发、安全或运维专题文档。
3. 核对源码、POM、配置、测试和 Git Diff；文档描述的目标状态不等于当前已实现状态。

## 强制边界

- 保持 `g2rain-department-startup → g2rain-department-biz → g2rain-department-api`。
- API 模块以查询契约为主；新增同步写契约必须有需求、权限、事务、幂等和兼容设计，并登记架构例外。
- Controller 只做协议适配，领域规则、事务和租户校验位于 Service。
- 部门、成员关系、权限组和策略数据必须保持可信上下文与 `organId` 一致。
- 绕过默认数据隔离的 DAO 查询必须由受信 Service 显式校验租户和访问权限。
- 不记录或返回密码、Token、密钥、验证码和不必要的个人信息。
- 数据库唯一键必须明确逻辑删除后的占位语义；需要释放业务键时使用中央 Profile 的函数索引规则。
- 修改 API、数据库、配置、模块、依赖或长期设计时同步更新文档。

## 完成前

- 执行 `mvn clean verify`，无法执行时准确报告原因和风险。
- 检查 Markdown 相对链接、`docs/project.yaml`、POM、端口、启动类和模块依赖。
- 按 `docs/development/definition-of-done.md` 核对结果。
- 不覆盖或提交与当前任务无关的 `logs/` 和用户修改。
