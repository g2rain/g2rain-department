package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * 部门批量添加用户 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "部门批量添加用户 DTO")
public class DepartmentAssignUsersDto {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "机构标识")
    private Long organId;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "部门标识")
    private Long departmentId;

    @Schema(description = "待添加用户标识集合")
    private Set<Long> userIds;
}
