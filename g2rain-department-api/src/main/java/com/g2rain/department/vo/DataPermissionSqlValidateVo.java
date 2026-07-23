package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SQL 数据权限隔离校验结果。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "SQL 数据权限隔离校验结果")
public class DataPermissionSqlValidateVo {

    @Schema(description = "所有需校验表均通过")
    private Boolean passed;

    @Schema(description = "顶层摘要信息")
    private String message;

    @Schema(description = "逐表校验结果")
    private List<DataPermissionSqlValidateTableVo> tables;
}
