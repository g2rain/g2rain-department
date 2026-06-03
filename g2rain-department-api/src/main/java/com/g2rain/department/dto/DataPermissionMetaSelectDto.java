package com.g2rain.department.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限元数据表查询入参DTO
 * 用于DataPermissionMetaDao.selectList方法的条件筛选
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限元数据表查询入参 DTO")
public class DataPermissionMetaSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 权限策略名称
     */
    @Schema(description = "权限策略名称")
    private String metaName;

    /**
     * 权限模型标识
     */
    @Schema(description = "权限模型标识")
    private Long modelId;

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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
