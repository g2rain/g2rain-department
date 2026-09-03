# 架构差异与接入项

项目在中央目录中的状态为 `planned`。以下事项完成决策、实现和验证前，不应宣称已正式采用 `java-domain-service 1.0.0`。

## DPT-001：IdP 同步写契约

`g2rain-department-api` 发布 `DepartmentIdpSyncApi.sync`，会在一个事务中创建或更新部门、映射和成员关系。这是具有业务语义的同步写命令，不是通用 CRUD，但仍属于中央 Profile 的同步写入例外。

- 当前保护：管理员校验、主体与请求 `organId` 一致、`READ_COMMITTED` 事务、全量破坏性对账显式开关。
- 待补：明确唯一受信调用方和服务身份校验，补充接口兼容、重试、幂等、审计和真实数据库事务测试。
- 复审：能够用可靠领域事件替代，或中央 Profile 调整同步写规则时。

## DPT-002：逻辑删除唯一索引

`department_idp_mapping.uk_organ_idp_dept` 当前使用普通 `UNIQUE (organ_id, idp_type, idp_dept_id, delete_flag)`。该结构只允许一条已删除历史记录，不满足“可反复删除并重建”的函数索引规则。

必须先决定 IdP 映射是“删除后释放”还是“永久占位”：前者迁移为 `IF(delete_flag = 0, 0, NULL)` 函数索引，后者唯一键不包含 `delete_flag`。迁移前需评估历史重复数据、执行顺序和回滚。

## DPT-003：运行端口不一致

应用默认端口是 `8085`，Jib 容器声明端口为 `8080`。部署清单、健康检查和 Gateway 路由可能因此失配。应统一端口或明确运行时映射，并同步 POM、配置和部署文档。

## DPT-004：开发凭证进入版本库

`application-dev.yml` 和 `codegen.properties` 当前包含本地数据库凭证。文档不会复制具体值；接入基线前应改为环境变量、私有本地覆盖或 Secret，并在必要时轮换已暴露凭证。

## DPT-005：验证覆盖不足

`2026-09-03` 执行 `mvn clean verify` 通过，`DepartmentIdpSyncServiceImplTest` 共 9 个测试通过，覆盖部分排序、映射、权限和安全闸逻辑。DAO/Mapper、租户隔离、Controller、缓存事件、真实事务和 Startup 组装尚无可见测试证据。
