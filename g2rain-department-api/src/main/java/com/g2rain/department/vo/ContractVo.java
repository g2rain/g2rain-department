package com.g2rain.department.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.g2rain.common.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;

import com.g2rain.common.json.ConditionalJsonIgnore;
import com.g2rain.common.json.AdminCompanyCondition;

/**
 * 客户表返回VO
 * 关联表名: contract
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户表 VO")
public class ContractVo extends BaseVo {

    /**
     * 租户/机构标识
     */
    @Schema(description = "租户/机构标识")
    private Long organId;

    /**
     * 
     */
    @Schema(description = "")
    private Long userId;

    /**
     * 
     */
    @Schema(description = "")
    private String deptPath;

    /**
     * 
     */
    @Schema(description = "")
    private String contractNo;

    /**
     * 
     */
    @Schema(description = "")
    private Long contractId;

    /**
     * 状态[ACTIVE:有效, INACTIVE:无效]
     */
    @Schema(description = "状态[ACTIVE:有效, INACTIVE:无效]")
    private String status;

    /**
     * 删除标志：0未删除，1已删除
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}