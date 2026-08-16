package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 部门 IdP 同步结果 VO。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "部门 IdP 同步结果")
public class DepartmentIdpSyncResultVo {

    @Schema(description = "新增部门数")
    private int departmentsCreated;

    @Schema(description = "更新部门数")
    private int departmentsUpdated;

    @Schema(description = "新增部门-成员关系数")
    private int relationsCreated;

    @Schema(description = "停用部门数（FULL 模式下快照外部门）")
    private int departmentsDisabled;

    @Schema(description = "移除部门-成员关系数（FULL 模式下关系对账）")
    private int relationsRemoved;
}
