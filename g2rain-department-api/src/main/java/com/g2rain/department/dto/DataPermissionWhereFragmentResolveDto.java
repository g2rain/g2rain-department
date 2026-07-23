package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据权限 WHERE 片段解析查询参数。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "数据权限 WHERE 片段解析查询参数")
public class DataPermissionWhereFragmentResolveDto {

    @Schema(description = "模块编码（可选，与 spring.application.name 一致；不传则返回全部模块）")
    private String moduleCode;

    @Schema(description = "业务表名（可选，不传则返回当前租户全部已配置 ACTIVE 策略的表）")
    private String tableName;
}
