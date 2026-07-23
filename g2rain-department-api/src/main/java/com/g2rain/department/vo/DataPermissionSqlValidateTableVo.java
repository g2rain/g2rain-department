package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SQL 隔离校验单表结果。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "SQL 隔离校验单表结果")
public class DataPermissionSqlValidateTableVo {

    @Schema(description = "物理表名")
    private String tableName;

    @Schema(description = "SQL 中的表别名")
    private String alias;

    @Schema(description = "未配置权限模型时为 true")
    private Boolean skipped;

    @Schema(description = "该表是否满足隔离")
    private Boolean passed;

    @Schema(description = "要求的完整隔离条件")
    private String requiredCondition;

    @Schema(description = "未通过原因")
    private String message;
}
