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
 * 数据权限元数据表查询DTO
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限元数据表 DTO")
public class DataPermissionMetaDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "机构标识")
    private Long organId;

    /**
     * 权限策略名称
     */
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "权限策略名称")
    private String metaName;

    /**
     * 权限模型标识
     */
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "权限模型标识")
    private Long modelId;

    /**
     * 是否读操作
     */
    @Schema(description = "是否读操作")
    private boolean read;

    /**
     * 是否写操作
     */
    @Schema(description = "是否写操作")
    private boolean write;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
