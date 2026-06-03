package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * 数据权限策略解析查询参数。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "数据权限策略解析查询参数")
public class DataPermissionPolicyResolveDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 用户标识
     */
    @Schema(description = "用户标识")
    private Long userId;

    /**
     * 逗号拼接的部门路径
     */
    @Schema(description = "逗号拼接的部门路径")
    private String deptPaths;

    /**
     * 模块编码
     */
    @Schema(description = "模块编码")
    private String moduleCode;

    /**
     * 业务表名
     */
    @Schema(description = "业务表名")
    private String tableName;

    /**
     * 部门路径集合
     */
    @Schema(hidden = true)
    private Set<String> deptPathSet;
}
