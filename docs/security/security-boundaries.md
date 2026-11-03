# 安全与租户边界

## 调用边界

- App 通过 IAM 获取 Token，再经 Gateway 访问公开接口。
- `policy_resolve`、`principal_enrichment` 和 IdP 同步属于受信能力；OpenAPI 隐藏不能替代认证和授权。
- IdP 同步当前要求管理员身份且主体 `organId` 与请求机构一致，调用方还应具备可验证的服务身份。

## 租户边界

- `organId` 必须来自可信上下文，不能只相信普通请求参数。
- 部门、成员关系、权限元数据、权限组、映射和策略查询必须限定在同一机构。
- `selectListWithoutIsolation` 等绕过隔离路径只能由受信 Service 调用，并补足机构校验。

## 数据权限策略边界

- `policy_resolve` 的 `deptPaths` 应来自受信主体增强或等价可信上下文，不能只信任普通客户端参数。
- `data_permission_other.permission_rule` 是会进入下游 SQL 的条件片段；管理端应按字段白名单生成，服务端与消费方不得接受任意未校验表达式。
- 当前 `groupRead` / `groupWrite` 来自 meta 默认读写位，不表示“用户已加入权限组”。权限组主要用于挂接 Other 例外规则；配置错误会导致权限过宽或过窄，见 [数据权限策略](../design/data-permission-policy.md)。
- 策略结果由 `g2rain-spring-boot-starter` 等消费方执行；本服务不保证消费方在策略为 `null` 或远程失败时的 fail-closed 行为。

## 高风险操作

- `FULL` IdP 同步只有显式开启 `enableDestructiveReconcile` 才允许停用部门或移除关系。
- 数据权限配置会改变下游可见和可写数据范围，应限制为管理员或数据治理角色并保留审计。
- 策略缓存失效事件失败可能造成旧权限持续生效，消费者必须具备 TTL、重试或兜底机制。

不得在配置、日志、响应和文档中暴露数据库密码、Nacos/Redis 凭证、Token、密钥、验证码或第三方原始敏感数据。
