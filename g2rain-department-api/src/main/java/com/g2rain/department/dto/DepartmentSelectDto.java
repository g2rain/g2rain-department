package com.g2rain.department.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 部门表查询入参DTO
 * 用于DepartmentDao.selectList方法的条件筛选
 * 表名: department
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门表查询入参 DTO")
public class DepartmentSelectDto extends BaseSelectListDto {

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
}
