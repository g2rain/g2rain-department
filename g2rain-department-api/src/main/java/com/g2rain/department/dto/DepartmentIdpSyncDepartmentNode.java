package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * IdP 部门同步节点。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "IdP 部门同步节点")
public class DepartmentIdpSyncDepartmentNode {

    @Schema(description = "IdP 侧部门标识")
    private String idpDeptId;

    @Schema(description = "IdP 侧父部门标识")
    private String parentIdpDeptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "排序")
    private Integer sortOrder;
}
