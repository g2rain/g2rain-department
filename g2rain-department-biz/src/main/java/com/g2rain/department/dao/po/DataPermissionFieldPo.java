package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限模型字段明细表返回Po
 * 关联表名: data_permission_field
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataPermissionFieldPo extends BasePo {

    /**
     * 权限模型标识
     */
    private Long modelId;

    /**
     * 业务表中的物理字段名，如 dept_path, owner_user_id
     */
    private String fieldName;

    /**
     * 前端显示的中文标签，如 所属部门, 负责人
     */
    private String fieldTitle;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
