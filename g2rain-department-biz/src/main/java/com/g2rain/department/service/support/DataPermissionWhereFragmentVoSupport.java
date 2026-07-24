package com.g2rain.department.service.support;

import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import com.g2rain.department.enums.DepartmentErrorCode;
import com.g2rain.department.vo.DataPermissionWhereFragmentVo;
import net.sf.jsqlparser.schema.Table;


/**
 * 填充单表 WHERE 片段 VO 的查询权限与提示信息。
 */
public final class DataPermissionWhereFragmentVoSupport {

    private DataPermissionWhereFragmentVoSupport() {
    }

    public static void enrich(
        DataPermissionWhereFragmentVo vo,
        DataPermissionPolicyResolveResult policy,
        boolean inGroup,
        String deptPathsCsv,
        Table table,
        DataIsolationMeta meta
    ) {
        boolean canQuery = DataPermissionWhereFragmentSupport.canQuery(policy, inGroup);
        if (canQuery) {
            String whereFragment = DataPermissionWhereFragmentSupport.buildGatedReadConditionFragment(
                table, meta, policy, inGroup, deptPathsCsv
            );
            vo.setHasQueryPermission(true);
            vo.setWhereFragment(whereFragment);
            vo.setMessage(null);
        } else {
            vo.setHasQueryPermission(false);
            vo.setWhereFragment(null);
            vo.setMessage(DepartmentErrorCode.DATA_PERMISSION_TABLE_QUERY_FORBIDDEN.messageTemplate());
        }
    }
}
