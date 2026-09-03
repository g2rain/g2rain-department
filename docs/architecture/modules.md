# 模块职责

```text
g2rain-department-startup
        -> g2rain-department-biz
        -> g2rain-department-api
```

| 模块 | 职责 | 约束 |
| --- | --- | --- |
| `g2rain-department-api` | 查询 DTO、VO、枚举及受信领域契约 | 不依赖 Biz/Startup，不发布 PO、Service 或通用远程 CRUD |
| `g2rain-department-biz` | Controller、Service、DAO、Converter、事务和领域规则 | 依赖 API，不依赖 Startup；业务规则不放入 Controller |
| `g2rain-department-startup` | 启动类、运行配置、Actuator 和镜像组装 | 不承载领域逻辑，不供下层反向依赖 |

当前 API 契约以查询为主；`DepartmentIdpSyncApi.sync` 是具有业务语义的同步写入例外，必须按[差异说明](deviations.md)治理。
