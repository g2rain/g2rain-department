package com.g2rain.department.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限模型全局元数据表查询DTO
 * 表名: data_permission_model
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限模型全局元数据表 DTO")
public class DataPermissionModelDto extends BaseDto {

    /**
     * 权限模型名称
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "权限模型名称")
    private String modelName;

    /**
     * 模块编码，如 order, crm, inventory
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "模块编码，如 order, crm, inventory")
    private String moduleCode;

    /**
     * 业务表名（建议小写）
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "业务表名（建议小写）")
    private String tableName;

    /**
     * 备注说明（如：订单主表权限模型）
     */
    @Schema(description = "备注说明（如：订单主表权限模型）")
    private String remark;
}
