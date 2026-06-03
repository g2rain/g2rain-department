package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 部门表返回Po
 * 关联表名: department
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentPo extends BasePo {

    /**
     * 父部门标识，根节点为0
     */
    private Long parentId;

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 全路径编码，如 00010001
     */
    private String deptPath;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 负责人用户标识
     */
    private Long leaderUserId;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    private String status;

    /**
     * 部门排序
     */
    private Integer sortOrder;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
