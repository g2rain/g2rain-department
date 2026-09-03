# 测试策略

| 层级 | 重点 |
| --- | --- |
| Domain/工具 | 部门路径、排序、权限模式转换和非法输入 |
| Service | 机构边界、状态、事务、批量关系、IdP 增量/全量同步和幂等 |
| DAO/Mapper | SQL、分页、逻辑删除、索引、数据隔离和策略聚合 |
| API/Controller | 参数校验、路由、结果和访问控制 |
| Startup | Bean、配置、Mapper 扫描、Profile 和端口 |

IdP 同步最低覆盖：父节点先于子节点、根节点处理、管理员校验、机构不匹配拒绝、增量不删除、全量默认不删除、显式安全闸后对账、重复同步、事务回滚和并发唯一键。

数据权限最低覆盖：部门与权限组聚合、读写位掩码、特殊规则、跨机构拒绝、无匹配策略、缓存失效范围和消费者重试。

```bash
mvn clean verify
mvn -pl g2rain-department-biz -am test
mvn -pl g2rain-department-biz -am -Dtest=DepartmentIdpSyncServiceImplTest test
```

Mock 单元测试不能证明真实数据库约束、AOP 事务和数据隔离行为。
