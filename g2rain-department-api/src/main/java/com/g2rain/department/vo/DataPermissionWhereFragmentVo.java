package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 单表数据权限 WHERE 片段。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "数据权限 WHERE 片段")
public class DataPermissionWhereFragmentVo {

    @Schema(description = "模块编码")
    private String moduleCode;

    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "OR 合并后的权限 WHERE 片段")
    private String whereFragment;

    @Schema(description = "当前用户是否具备查询权限")
    private boolean hasQueryPermission;

    @Schema(description = "无查询权限时的提示")
    private String message;
}
