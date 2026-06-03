package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据权限策略解析结果。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "数据权限策略解析结果")
public class DataPermissionPolicyVo {

    @Schema(description = "权限策略标识")
    private Long metaId;

    @Schema(description = "分组维度是否可读")
    private boolean groupRead;

    @Schema(description = "分组维度是否可写")
    private boolean groupWrite;

    @Schema(description = "Other 规则是否可读")
    private boolean otherRead;

    @Schema(description = "Other 规则是否可写")
    private boolean otherWrite;

    @Schema(description = "Other 权限规则 SQL 片段")
    private String otherPermRule;
}
