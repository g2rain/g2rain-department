# API 设计规范

## 公开接口

管理端写接口由 App 携带 IAM Token 经 Gateway 调用。路径沿用现有 snake_case，如 `/department_user_relation`。查询返回 `Result<T>`，分页返回 `Result<PageData<T>>`。

## API 模块契约

- `DepartmentApi`：部门列表、分页和树查询。
- `DepartmentUserRelationApi`：关系查询及受信的主体增强查询。
- 数据权限各 API：模型、字段、元数据、权限组和特殊规则查询。
- `DataPermissionMetaApi.policy_resolve`：受信策略解析查询，当前在 OpenAPI 中隐藏。
- `DepartmentIdpSyncApi`：IdP 同步命令和映射查询；同步命令属于登记中的架构例外。

### `policy_resolve` 契约摘要

- 路径：`GET /data_permission_meta/policy_resolve`
- 入参：`organId`、`userId`、`deptPaths`（逗号分隔）、`moduleCode`、`tableName`
- 出参：`DataPermissionPolicyVo`（`metaId`、`groupRead`/`groupWrite`、`otherRead`/`otherWrite`、`otherPermRule`）；无匹配 meta 时 data 可为 `null`
- 调用方：数据隔离客户端等受信服务；`deptPaths` 应来自主体增强
- 语义细节与字段含义见 [数据权限策略](../design/data-permission-policy.md)

OpenAPI `hidden = true`、内部路径或内网部署都不能替代认证、服务身份和授权。新增写契约必须说明调用方、幂等、事务、重试和版本兼容。
