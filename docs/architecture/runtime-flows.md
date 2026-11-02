# 核心运行流程

## 部门与成员维护

App 经 IAM、Gateway 调用写接口；Controller 绑定参数，Service 校验机构和层级后通过 DAO 写入 MySQL。部门负责人或成员关系变化时，广播受影响用户的策略缓存失效范围。

## IdP 部门同步

受信调用方提交机构、IdP 类型、部门快照和成员关系。Service 校验当前主体为管理员且请求 `organId` 与主体一致，按父节点优先创建或更新部门。`FULL` 模式只有显式开启 `enableDestructiveReconcile` 才停用缺失部门或删除失效关系。完整约束见 [IdP 部门同步](../design/idp-department-sync.md)。

## 数据权限策略

管理端维护模型、字段、租户元数据、权限组、成员关系和特殊规则。受信客户端调用 `policy_resolve`，服务按机构、用户、部门路径、模块和表聚合策略：

1. 匹配机构内 ACTIVE meta，将其 `permission_mode` 输出为 `groupRead` / `groupWrite`（**不**依赖用户是否属于权限组）。
2. 按用户 + 组 `dept_path` 精确命中权限组，聚合该组上绑定到该 meta 的 Other 规则为 `otherRead` / `otherWrite` / `otherPermRule`。
3. 无匹配 meta 时返回 `null`；下游隔离组件负责决定如何处理空策略。

配置变化通过 `g2rain-syncer` 通知消费者失效本地缓存。完整语义见 [数据权限策略](../design/data-permission-policy.md)。
