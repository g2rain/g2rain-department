# 故障排查

## 无法启动

确认 JDK 25、数据源、MySQL 数据库、Mapper 路径和当前 Profile。启用 Nacos 时检查地址、命名空间、group 和凭证。

## App 无法访问接口

确认 IAM Token、Gateway 路由、服务发现和权限配置。受信接口不能由普通 App 直接访问。

## IdP 同步失败或结果异常

检查调用主体是否为管理员、请求与主体 `organId` 是否一致、父部门是否在子部门之前可解析。全量模式默认不会删除；只有显式安全闸开启后才执行破坏性对账。

## 策略结果或缓存异常

从 `organId`、用户、部门路径、模块、表名、模型、元数据、权限组和特殊规则逐项核对。配置变化后检查 `g2rain-syncer` 输出绑定和消费者缓存 TTL。

常见配置误解：

| 现象 | 可能原因 |
| --- | --- |
| 未加入权限组仍有部门范围过滤 | 当前设计下 `groupRead`/`groupWrite` 来自 meta，不依赖组成员；权限组主要影响 Other 规则 |
| 已配置 Other 规则但不生效 | 组 `dept_path` 与主体 `deptPaths` 不是精确匹配；或组/关系/规则非 ACTIVE；或用户未加入该组 |
| 有 meta、无部门过滤 | 消费方未配置 `permissionTableName` / `deptPathColumnName`，或 Principal 缺少 `deptPath`，或 `module_code` 与 `spring.application.name` 不一致 |
| 策略变更后仍用旧结果 | 消费方本地缓存未收到失效事件；检查 syncer 与 TTL |
| 期望“无策略即拒绝”但机构内仍全量可见 | 当前 starter 在策略为 `null` 或解析失败时仅保留机构隔离（fail-open）；见 starter 隔离文档与偏差登记 |

## 镜像健康检查失败

应用默认监听并由 Jib 声明暴露 `8085`；检查 `SERVER_PORT` 覆盖值、Service 和健康检查是否一致。
