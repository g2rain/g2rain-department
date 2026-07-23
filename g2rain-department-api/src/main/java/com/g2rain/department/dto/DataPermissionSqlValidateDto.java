package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义 SELECT SQL 数据权限隔离校验参数。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "SQL 数据权限隔离校验参数")
public class DataPermissionSqlValidateDto {

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "模块编码，与调用方 spring.application.name 一致")
    private String moduleCode;

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "待校验 SELECT SQL")
    private String sql;
}
