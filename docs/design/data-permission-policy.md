# 数据权限策略

本文档描述 `g2rain-department` 当前实现的数据权限控制面语义。下游执行由 `g2rain-spring-boot-starter` 的 MyBatis 数据隔离组件完成；本服务只解析策略并广播缓存失效，不改写业务 SQL。

## 产品语义（当前）

当前实现是 **机构内默认部门范围 + 权限组挂接例外规则**，不是“必须加入权限组才获得部门数据权限”的 RBAC 组模型。

| 概念 | 当前含义 |
| --- | --- |
| `data_permission_meta.permission_mode` | 机构内对该模型的默认读写位；映射到策略结果的 `groupRead` / `groupWrite` |
| `data_permission_group` | 挂在 `organId + deptPath` 下的权限小组，用于关联成员与 Other 规则 |
| 组成员资格 | 仅决定用户能否命中该组上的 `data_permission_other` 规则 |
| `groupRead` / `groupWrite` | **不**要求用户属于任一权限组；只要存在匹配的 ACTIVE meta，即按其 `permission_mode` 输出 |

若产品目标改为“未加入权限组则无部门范围”，需要同步修改 `policy_resolve` SQL 与本文档，并做联调验证。

## 模型

| 表 | 职责 |
| --- | --- |
| `data_permission_model` | 全局声明：`module_code`（通常等于消费方 `spring.application.name`）与 `table_name` |
| `data_permission_field` | 允许参与 Other 规则编辑的字段白名单（管理端使用） |
| `data_permission_meta` | 机构内对某模型的策略配置，含 `permission_mode` 与启停状态 |
| `data_permission_group` | 权限小组：`organ_id`、`dept_path`、`group_name`、状态 |
| `data_permission_group_user_relation` | 小组成员关系 |
| `data_permission_other` | 绑定 `meta_id + group_id` 的特殊规则：`permission_mode` + `permission_rule`（SQL 条件片段） |

`permission_mode` 位掩码：bit0 = 写，bit1 = 读（`--=0`，`-w=1`，`r-=2`，`rw=3`）。

部门路径 `dept_path` 采用固定 4 位 36 进制段编码（见 `DeptUtils`）。消费方对部门维度使用前缀匹配 `LIKE '{path}%'` 时，不会与同级兄弟路径冲突。

## 解析：`policy_resolve`

### 入参

受信客户端提交：

- `organId`、`userId`
- `deptPaths`：逗号分隔的部门路径（应来自主体增强，不能信任浏览器随意伪造）
- `moduleCode`、`tableName`

### 聚合逻辑（当前实现）

1. 按 `moduleCode + tableName` 找到模型，再匹配机构内 ACTIVE 且未删除的 meta。
2. 在同一机构下，查找用户 ACTIVE 成员关系命中的权限组，且组的 `dept_path` **精确等于** 入参 `deptPaths` 中某一项。
3. 将命中组上的 ACTIVE `data_permission_other` 按 meta 关联：
   - `other_permission_mode` = 各规则 `permission_mode` 的 `BIT_OR`
   - `otherPermRule` = 各 `permission_rule` 以 `(rule) OR (rule)` 拼接
4. `group_permission_mode` 直接取自 **meta.permission_mode**，不判断是否命中权限组。

无匹配 meta 时返回 `null`。

### 出参

| 字段 | 含义 |
| --- | --- |
| `metaId` | 命中的权限元数据 ID |
| `groupRead` / `groupWrite` | 由 meta `permission_mode` 解码；供消费方启用部门路径过滤 |
| `otherRead` / `otherWrite` | 由命中组的 Other 规则 `permission_mode` 聚合解码 |
| `otherPermRule` | Other 规则 SQL 条件片段（可能含多个 `OR` 分支） |

### 消费约定

调用方（典型为 `g2rain-starter-mybatis-extensions`）负责把结果安全地应用于 SQL：

- 机构过滤始终由隔离组件单独施加。
- 动态权限条件通常为：用户维度 OR 部门维度 OR Other 规则，再与机构条件 AND。
- 不得把未经验证的任意表达式拼进 SQL；Other 规则应按受信配置与字段约定生成。

组匹配使用 `dept_path IN (...)`（精确），隔离执行使用 `LIKE '{path}%'`（含下级）。配置权限组时，组的 `dept_path` 必须出现在用户主体增强得到的路径集合中，否则 Other 规则不会生效。

## 配置与管理端流程

推荐配置顺序：

1. 定义 `data_permission_model` / `data_permission_field`
2. 为机构创建 `data_permission_meta` 并设置默认读写位
3. 创建权限组（指定所属 `dept_path`）并分配成员
4. 为「meta + 组」配置 `data_permission_other` 例外规则
5. 变更后依赖缓存失效广播；消费方以 TTL / 兜底刷新消化延迟

## 缓存一致性

模型、元数据、权限组、成员关系、特殊规则、部门成员或负责人变化后，`DataPermissionPolicyCacheBroadcaster` 通过 `g2rain-syncer` 发布相应作用域的失效事件。消息可能重复、延迟或失败，消费者必须设计幂等、TTL 和兜底刷新。

## 相关文档

- 运行流程：[runtime-flows.md](../architecture/runtime-flows.md)
- 安全边界：[security-boundaries.md](../security/security-boundaries.md)
- API 约定：[api-conventions.md](../development/api-conventions.md)
- 消费方隔离行为：`g2rain-spring-boot-starter` 的 `g2rain-starter-mybatis-extensions/README.md`
