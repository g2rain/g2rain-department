# 数据库与数据模型

数据库初始化脚本为 `scripts/g2rain-department.sql`，当前包含：

- `department`
- `department_user_relation`
- `department_idp_mapping`
- `data_permission_model`
- `data_permission_field`
- `data_permission_meta`
- `data_permission_group`
- `data_permission_group_user_relation`
- `data_permission_other`

租户表必须在查询、写入、索引和业务校验中保持 `organ_id` 一致。使用 `WithoutIsolation` 查询时，受信 Service 必须显式验证机构边界。

### 数据权限相关约定

- `department.dept_path` / `data_permission_group.dept_path` 使用固定 4 位段编码，保证消费方 `LIKE '{path}%'` 不会误匹配兄弟节点。
- `data_permission_meta` 在同一 `organ_id + model_id` 下业务上应唯一（服务层有重复校验）；DDL 以脚本与迁移为准。
- `data_permission_group_user_relation` 当前以索引支持查询；业务层应防止同一用户在同组重复有效关系。
- `data_permission_other` 以 `(meta_id, group_id)` 关联策略与权限组；`permission_rule` 存 SQL 条件片段，变更需评估下游隔离安全。
- 策略语义见 [数据权限策略](../design/data-permission-policy.md)。

逻辑删除唯一键必须声明语义：

- 删除后允许重建：使用 `(IF(delete_flag = 0, 0, NULL))` 函数唯一索引。
- 删除后永久占位：唯一键不包含 `delete_flag`。

当前 `department_idp_mapping` 的普通 `UNIQUE (..., delete_flag)` 尚待整改，见[架构差异](../architecture/deviations.md)。初始化脚本与生产增量迁移必须分离，结构变化需说明历史数据、执行顺序、验证和回滚。
