# 安全与租户边界

## 调用边界

- App 通过 IAM 获取 Token，再经 Gateway 访问公开接口。
- `policy_resolve`、`principal_enrichment` 和 IdP 同步属于受信能力；OpenAPI 隐藏不能替代认证和授权。
- IdP 同步当前要求管理员身份且主体 `organId` 与请求机构一致，调用方还应具备可验证的服务身份。

## 租户边界

- `organId` 必须来自可信上下文，不能只相信普通请求参数。
- 部门、成员关系、权限元数据、权限组、映射和策略查询必须限定在同一机构。
- `selectListWithoutIsolation` 等绕过隔离路径只能由受信 Service 调用，并补足机构校验。

## 高风险操作

- `FULL` IdP 同步只有显式开启 `enableDestructiveReconcile` 才允许停用部门或移除关系。
- 数据权限配置会改变下游可见和可写数据范围，应限制为管理员或数据治理角色并保留审计。
- 策略缓存失效事件失败可能造成旧权限持续生效，消费者必须具备 TTL、重试或兜底机制。

不得在配置、日志、响应和文档中暴露数据库密码、Nacos/Redis 凭证、Token、密钥、验证码或第三方原始敏感数据。
