package com.g2rain.department.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "关联 data_permission_meta.id")
    private Long metaId;

    /**
     * 分组标识
     */
    @Schema(description = "分组标识")
    private Long groupId;

    /**
     * 权限模式[例如 rw]
     */
    @Schema(description = "权限模式[例如 rw]")
    private Byte permissionMode;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    @Schema(description = "状态[ACTIVE:有效, INACTIVE:停用]")
    private String status;

    /**
     * 权限规则
     */
    @Schema(description = "权限规则")
    private String permissionRule;
}
