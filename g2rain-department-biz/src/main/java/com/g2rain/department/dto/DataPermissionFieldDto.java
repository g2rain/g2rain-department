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
 * 数据权限模型字段明细表查询DTO
 * 表名: data_permission_field
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限模型字段明细表 DTO")
public class DataPermissionFieldDto extends BaseDto {

    /**
     * 权限模型标识
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "权限模型标识")
    private Long modelId;

    /**
     * 业务表中的物理字段名，如 dept_path, owner_user_id
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "业务表中的物理字段名，如 dept_path, owner_user_id")
    private String fieldName;

    /**
     * 前端显示的中文标签，如 所属部门, 负责人
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "前端显示的中文标签，如 所属部门, 负责人")
    private String fieldTitle;

    /**
     * 排序
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "排序")
    private Integer sortOrder;
}
