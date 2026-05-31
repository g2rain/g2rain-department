package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 部门人员关系表返回Po
 * 关联表名: department_user_relation
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentUserRelationPo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 部门标识
     */
    private Long departmentId;

    /**
     * 用户标识
     */
    private Long userId;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
