package com.g2rain.department.service.support;

import com.g2rain.common.web.PrincipalContextHolder;
import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import com.g2rain.data.isolation.sql.DataPermissionConditionBuilder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

import java.util.Objects;

/**
 * 构建数据权限读条件表达式及 WHERE 片段字符串。
 */
public final class DataPermissionWhereFragmentSupport {

    private DataPermissionWhereFragmentSupport() {
    }

    public static boolean canQuery(DataPermissionPolicyResolveResult policy, boolean inGroup) {
        return Objects.nonNull(policy) && policy.isGroupRead() && inGroup;
    }

    public static Expression buildReadConditionExpression(
        Table table,
        DataIsolationMeta meta,
        DataPermissionPolicyResolveResult policy,
        String deptPathsCsv
    ) {
        String previousDeptPath = PrincipalContextHolder.getDeptPath();
        try {
            PrincipalContextHolder.setDeptPath(deptPathsCsv);
            return DataPermissionConditionBuilder.buildReadCondition(table, meta, policy);
        } finally {
            PrincipalContextHolder.setDeptPath(previousDeptPath);
        }
    }

    public static String buildReadConditionFragment(
        Table table,
        DataIsolationMeta meta,
        DataPermissionPolicyResolveResult policy,
        String deptPathsCsv
    ) {
        Expression expression = buildReadConditionExpression(table, meta, policy, deptPathsCsv);
        return Objects.isNull(expression) ? null : expression.toString();
    }

    public static Expression buildGatedReadConditionExpression(
        Table table,
        DataIsolationMeta meta,
        DataPermissionPolicyResolveResult policy,
        boolean inGroup,
        String deptPathsCsv
    ) {
        if (!canQuery(policy, inGroup)) {
            return null;
        }
        return buildReadConditionExpression(table, meta, policy, deptPathsCsv);
    }

    public static String buildGatedReadConditionFragment(
        Table table,
        DataIsolationMeta meta,
        DataPermissionPolicyResolveResult policy,
        boolean inGroup,
        String deptPathsCsv
    ) {
        Expression expression = buildGatedReadConditionExpression(table, meta, policy, inGroup, deptPathsCsv);
        return Objects.isNull(expression) ? null : expression.toString();
    }
}
