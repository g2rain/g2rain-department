# 代码规范

- 模块依赖固定为 `startup → biz → api`。
- Controller 负责路由、绑定、校验入口和 `Result<T>` 包装；Service 负责领域规则、事务、幂等和租户校验；DAO 只负责持久化。
- API 模块保存可复用查询 DTO、VO、枚举及少量明确的受信契约；Biz 写入 DTO 不作为跨服务契约。
- 新增部门、成员或权限策略数据时使用平台 `IdGenerator`，并保持请求、主体和记录的 `organId` 一致。
- MapStruct Converter 只做对象转换，不访问数据库和远程服务。
- 环境差异使用环境变量、Profile、Nacos 或 Secret，不在 Java、YAML 和属性文件中写入真实凭证。
- 日志不得记录 Token、密码、密钥、验证码或完整第三方数据。
- 修改数据库同步更新 SQL 和迁移说明；修改长期边界增加 ADR 或架构差异。
