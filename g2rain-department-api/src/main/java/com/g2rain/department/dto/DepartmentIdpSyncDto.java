package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门 IdP 同步请求 DTO。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "部门 IdP 同步请求 DTO")
public class DepartmentIdpSyncDto {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "机构标识")
    private Long organId;

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "身份源类型")
    private String idpType;

    @Schema(description = "同步模式，默认 INCREMENTAL",
        allowableValues = {"FULL", "INCREMENTAL"})
    private String syncMode = "INCREMENTAL";

    @Valid
    @Schema(description = "IdP 部门列表")
    private List<DepartmentIdpSyncDepartmentNode> departments = new ArrayList<>();

    @Valid
    @Schema(description = "成员部门关联列表")
    private List<DepartmentIdpSyncMemberDepartment> memberDepartments = new ArrayList<>();

    @Schema(description = "是否允许 FULL 对账删除（停用部门、清理关系）；默认 true")
    private Boolean enableDestructiveReconcile = true;
}
