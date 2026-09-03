# 数据权限策略

## 模型

- `data_permission_model`：模块和业务表的全局模型。
- `data_permission_field`：允许参与策略的字段。
- `data_permission_meta`：机构内模型配置和读写模式。
- `data_permission_group` / `data_permission_group_user_relation`：按部门路径和成员建立权限分组。
- `data_permission_other`：针对组和元数据的特殊规则。

## 解析

受信客户端以机构、用户、部门路径、模块和表为上下文调用 `policy_resolve`。服务聚合模型、租户元数据、权限组和特殊规则，返回读写能力及规则。调用方负责把结果安全地应用于实际 SQL，不得拼接未经验证的任意表达式。

## 缓存一致性

模型、元数据、权限组、成员关系、特殊规则、部门成员或负责人变化后，`DataPermissionPolicyCacheBroadcaster` 通过 `g2rain-syncer` 发布相应作用域的失效事件。消息可能重复、延迟或失败，消费者必须设计幂等、TTL 和兜底刷新。
