package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 客户表返回Po
 * 关联表名: contract
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContractPo extends BasePo {

    /**
     * 租户/机构标识
     */
    private Long organId;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String deptPath;

    /**
     * 
     */
    private String contractNo;

    /**
     * 
     */
    private Long contractId;

    /**
     * 状态[ACTIVE:有效, INACTIVE:无效]
     */
    private String status;

    /**
     * 删除标志：0未删除，1已删除
     */
    private Boolean deleteFlag;
}