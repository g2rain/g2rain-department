package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限模型全局元数据表返回Po
 * 关联表名: data_permission_model
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataPermissionModelPo extends BasePo {

    /**
     * 权限模型名称
     */
    private String modelName;

    /**
     * 模块编码，如 order, crm, inventory
     */
    private String moduleCode;

    /**
     * 业务表名（建议小写）
     */
    private String tableName;

    /**
     * 是否启用 USER 维度
     */
    private Boolean enableUserScope;

    /**
     * 是否启用 DEPT 维度
     */
    private Boolean enableDeptScope;

    /**
     * 备注说明（如：订单主表权限模型）
     */
    private String remark;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
