package com.g2rain.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 成员与 IdP 部门关联同步节点。
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "成员 IdP 部门关联同步节点")
public class DepartmentIdpSyncMemberDepartment {

    @Schema(description = "平台用户标识")
    private Long userId;

    @Schema(description = "IdP 侧部门标识列表")
    private Set<String> idpDeptIds = new LinkedHashSet<>();
}
