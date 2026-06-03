package com.g2rain.department.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 部门人员关系表查询入参DTO
 * 用于DepartmentUserRelationDao.selectList方法的条件筛选
 * 表名: department_user_relation
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门人员关系表查询入参 DTO")
public class DepartmentUserRelationSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 部门标识
     */
    @Schema(description = "部门标识")
    private Long departmentId;

    /**
     * 用户标识
     */
    @Schema(description = "用户标识")
    private Long userId;

    /**
     * 用户标识集合
     */
    @Schema(description = "用户标识集合")
    private java.util.Set<Long> userIds;
}
