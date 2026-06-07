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
 * 数据权限Other规则表查询DTO
 * 表名: data_permission_other
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限Other规则表 DTO")
public class DataPermissionOtherDto extends BaseDto {

    /**
     * 关联 data_permission_meta.id
     */
    @NotNull
    @Schema(description = "关联 data_permission_meta.id")
    private Long metaId;

    /**
     * 分组标识
     */
    @NotNull
    @Schema(description = "分组标识")
    private Long groupId;

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
     * 权限规则
     */
    @NotBlank
    @Schema(description = "权限规则")
    private String permissionRule;
}
