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
 * 部门表返回VO
 * 关联表名: department
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门表 VO")
public class DepartmentVo extends BaseVo {

    /**
     * 父部门标识，根节点为0
     */
    @Schema(description = "父部门标识，根节点为0")
    private Long parentId;

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 当前层级编码（固定4位步长）
     */
    @Schema(description = "当前层级编码（固定4位步长）")
    private String deptCode;

    /**
     * 全路径编码，如 00010001
     */
    @Schema(description = "全路径编码，如 00010001")
    private String deptPath;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 负责人用户标识
     */
    @Schema(description = "负责人用户标识")
    private Long leaderUserId;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    @Schema(description = "状态[ACTIVE:有效, INACTIVE:停用]")
    private String status;

    /**
     * 部门排序
     */
    @Schema(description = "部门排序")
    private Integer sortOrder;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}
