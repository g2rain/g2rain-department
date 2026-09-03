# 配置说明

主配置位于 `g2rain-department-startup/src/main/resources/application.yml`，本地数据源位于 `application-dev.yml`。

| 配置 | 默认值 | 说明 |
| --- | --- | --- |
| `SERVER_PORT` | `8085` | HTTP 端口 |
| `SPRING_PROFILES_ACTIVE` | `dev` | 激活的 Profile |
| `NACOS_SERVER_ADDR` | `127.0.0.1:8848` | Nacos 地址 |
| `SPRING_CLOUD_NACOS_DISCOVERY_NAMESPACE` | `dev` | 注册中心命名空间 |
| `SPRING_CLOUD_NACOS_CONFIG_NAMESPACE` | `dev` | 配置中心命名空间 |

服务名为 `g2rain-department`，discovery group 为 `g2rain`，配置 group 使用服务名。MyBatis Mapper 路径为 `classpath:/mybatis/mapper/*.xml`。Redis Stream 输出绑定 `output`，目标为 `g2rain-syncer`。

开发配置和代码生成配置当前存在版本库凭证风险。共享、测试和生产环境必须由环境变量、Secret 或 Nacos 注入，不得复用仓库中的本地值。
