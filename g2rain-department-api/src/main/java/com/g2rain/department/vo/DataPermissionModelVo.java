package com.g2rain.department.vo;

import com.g2rain.common.json.AdminCompanyCondition;
import com.g2rain.common.json.ConditionalJsonIgnore;
import com.g2rain.common.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据权限模型全局元数据表返回VO
 * 关联表名: data_permission_model
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限模型全局元数据表 VO")
public class DataPermissionModelVo extends BaseVo {

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

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}
