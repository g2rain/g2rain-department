package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * 权限小组批量添加用户 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "权限小组批量添加用户 DTO")
public class GroupAssignUsersDto {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "机构标识")
    private Long organId;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "分组标识")
    private Long groupId;

    @Schema(description = "待添加用户标识集合")
    private Set<Long> userIds;
}
