package com.g2rain.department.dao.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据权限策略解析结果 PO。
 */
@Getter
@Setter
@NoArgsConstructor
public class DataPermissionPolicyPo {

    private Long metaId;

    private Integer groupPermissionMode;

    private Integer otherPermissionMode;

    private String permissionRule;

    private Integer inGroup;
}
