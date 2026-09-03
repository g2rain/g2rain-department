# 代码生成

根 POM 配置 `g2rain-crafter`，`codegen.properties` 定义基础包、数据库连接、表列表和覆盖开关。

```bash
mvn g2rain-crafter:bootstrap
```

生成前必须：

1. 提交或备份当前修改。
2. 使用最小权限的开发数据库账号，并通过私有覆盖提供密码。
3. 明确目标表，保持 `tables.overwrite=false`，除非已经审查覆盖差异。
4. 生成后逐文件检查 Controller、DTO、Service、DAO、Mapper、租户隔离和 API/Biz 边界。
5. 执行 `mvn clean verify` 并同步文档。

生成结果是实现起点，不自动满足领域规则、安全边界和架构基线。
