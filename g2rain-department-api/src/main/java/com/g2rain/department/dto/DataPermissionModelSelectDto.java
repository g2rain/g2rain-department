package com.g2rain.department.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限模型全局元数据表查询入参DTO
 * 用于DataPermissionModelDao.selectList方法的条件筛选
 * 表名: data_permission_model
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限模型全局元数据表查询入参 DTO")
public class DataPermissionModelSelectDto extends BaseSelectListDto {

    /**
     * 模块编码，如 order, crm, inventory
     */
    @Schema(description = "模块编码，如 order, crm, inventory")
    private String moduleCode;

    /**
     * 业务表名（建议小写）
     */
    @Schema(description = "业务表名（建议小写）")
    private String tableName;

    /**
     * 备注说明（如：订单主表权限模型）
     */
    @Schema(description = "备注说明（如：订单主表权限模型）")
    private String remark;
}
