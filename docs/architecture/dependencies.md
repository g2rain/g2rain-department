# 依赖边界

## 编译期依赖

- API：Spring Web、Validation 与 `g2rain-common` 公共模型。
- Biz：API、MyBatis、MySQL、Nacos、Aegis、缓存同步、Redis Stream 和数据隔离 Starter。
- Startup：Biz、Spring MVC、Actuator、SpringDoc 与 Jib。

## 平台协作

| 协作者 | 关系 |
| --- | --- |
| `g2rain-basis` | 提供用户、机构等主数据语义；当前仓库没有直接编译依赖 |
| `g2rain-iam` | 为 App 和受信调用链提供身份与 Token |
| Gateway | 承载公开接口的统一入口、鉴权和转发 |
| `g2rain-department-app` | 部门与数据权限管理界面 |
| 数据隔离 Starter | 查询主体增强和策略并执行下游 SQL 隔离 |
| `g2rain-syncer` | 传播权限策略缓存失效事件 |

同步依赖默认用于查询。新增跨服务写入必须先建立需求和安全契约，不能通过复用 Biz DTO 绕过本服务领域规则。
