package com.g2rain.department.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 部门表查询DTO
 * 表名: department
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门表 DTO")
public class DepartmentDto extends BaseDto {

    /**
     * 父部门标识，根节点为0
     */
    @Schema(description = "父部门标识，根节点为0")
    private Long parentId;

    /**
     * 机构标识
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "机构标识")
    private Long organId;

    /**
     * 部门名称
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "部门名称")
    private String deptName;

    /**
     * 负责人用户标识
     */
    @Schema(description = "负责人用户标识")
    private Long leaderUserId;

    /**
     * 部门排序
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "部门排序")
    private Integer sortOrder;
}
