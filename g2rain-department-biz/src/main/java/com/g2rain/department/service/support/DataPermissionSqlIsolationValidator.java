package com.g2rain.department.service.support;

import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import com.g2rain.department.vo.DataPermissionSqlValidateTableVo;
import com.g2rain.department.vo.DataPermissionSqlValidateVo;
import com.g2rain.mybatis.extension.SqlParserDelegate;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 从 SQL 提取物理表并校验数据权限隔离条件。
 */
public final class DataPermissionSqlIsolationValidator {

    private DataPermissionSqlIsolationValidator() {
    }

    public record SqlTableReference(Table table, PlainSelect plainSelect) {
    }

    public static List<SqlTableReference> extractTableReferences(String sql) throws JSQLParserException {
        Statement statement = SqlParserDelegate.parse(sql);
        if (!(statement instanceof Select select)) {
            throw new JSQLParserException("unsupported statement for data permission validation");
        }
        List<SqlTableReference> references = new ArrayList<>();
        collectFromSelect(select, references);
        return references;
    }

    public static DataPermissionSqlValidateVo validate(
        String sql,
        String moduleCode,
        Long organId,
        String deptPathsCsv,
        Function<String, DataPermissionIsolationContext> contextResolver
    ) {
        DataPermissionSqlValidateVo result = new DataPermissionSqlValidateVo();

        List<SqlTableReference> references;
        try {
            references = extractTableReferences(sql);
        } catch (JSQLParserException ex) {
            result.setPassed(false);
            result.setMessage("无法解析 SQL");
            result.setTables(List.of());
            return result;
        }

        if (references.isEmpty()) {
            result.setPassed(false);
            result.setMessage("未识别到可校验的物理表");
            result.setTables(List.of());
            return result;
        }

        Map<String, DataPermissionSqlValidateTableVo> deduplicated = new LinkedHashMap<>();
        boolean hasValidatable = false;
        boolean allPassed = true;
        String firstFailure = null;

        for (SqlTableReference reference : references) {
            String tableName = normalizeTableName(reference.table());
            String dedupeKey = tableName + "|" + aliasKey(reference.table());
            if (deduplicated.containsKey(dedupeKey)) {
                continue;
            }

            DataPermissionSqlValidateTableVo item = new DataPermissionSqlValidateTableVo();
            item.setTableName(tableName);
            item.setAlias(aliasName(reference.table()));

            DataPermissionIsolationContext context = contextResolver.apply(tableName);
            if (Objects.isNull(context) || Objects.isNull(context.getModelPo())) {
                item.setSkipped(true);
                item.setPassed(true);
                deduplicated.put(dedupeKey, item);
                continue;
            }

            hasValidatable = true;
            item.setSkipped(false);

            DataIsolationMeta meta = context.getIsolationMeta();
            DataPermissionPolicyResolveResult policy = context.getPolicy();
            Expression permissionExpr = DataPermissionWhereFragmentSupport.buildReadConditionExpression(
                reference.table(), meta, policy, deptPathsCsv
            );
            Expression requiredCondition = buildRequiredCondition(reference.table(), meta, organId, permissionExpr);
            item.setRequiredCondition(requiredCondition.toString());

            Expression userWhere = reference.plainSelect().getWhere();
            boolean passed = WhereExpressionCoverageChecker.covers(userWhere, requiredCondition);
            item.setPassed(passed);
            if (!passed) {
                allPassed = false;
                item.setMessage(resolveFailureMessage(userWhere, meta, organId, permissionExpr));
                if (!StringUtils.hasText(firstFailure)) {
                    firstFailure = tableName + ": " + item.getMessage();
                }
            }
            deduplicated.put(dedupeKey, item);
        }

        List<DataPermissionSqlValidateTableVo> tableResults = new ArrayList<>(deduplicated.values());
        result.setTables(tableResults);

        if (!hasValidatable) {
            result.setPassed(true);
            result.setMessage("无可校验的数据权限表");
            return result;
        }

        result.setPassed(allPassed);
        result.setMessage(allPassed ? null : firstFailure);
        return result;
    }

    private static void collectFromSelect(Select select, List<SqlTableReference> references) {
        if (Objects.nonNull(select.getPlainSelect())) {
            collectFromPlainSelect(select.getPlainSelect(), references);
            return;
        }
        if (select.getSelectBody() instanceof PlainSelect plainSelect) {
            collectFromPlainSelect(plainSelect, references);
            return;
        }
        if (select.getSelectBody() instanceof SetOperationList setOperationList) {
            for (var body : setOperationList.getSelects()) {
                if (body instanceof PlainSelect plainSelect) {
                    collectFromPlainSelect(plainSelect, references);
                } else if (body instanceof ParenthesedSelect parenthesedSelect
                    && parenthesedSelect.getSelect() instanceof PlainSelect plainSelect) {
                    collectFromPlainSelect(plainSelect, references);
                }
            }
        }
    }

    private static void collectFromPlainSelect(PlainSelect plainSelect, List<SqlTableReference> references) {
        collectFromFromItem(plainSelect.getFromItem(), plainSelect, references);
        if (Objects.nonNull(plainSelect.getJoins())) {
            for (Join join : plainSelect.getJoins()) {
                collectFromFromItem(join.getRightItem(), plainSelect, references);
            }
        }
    }

    private static void collectFromFromItem(FromItem fromItem, PlainSelect owner, List<SqlTableReference> references) {
        if (fromItem instanceof Table table) {
            references.add(new SqlTableReference(normalizeTable(table), owner));
            return;
        }
        if (fromItem instanceof ParenthesedSelect parenthesedSelect) {
            Select innerSelect = parenthesedSelect.getSelect();
            if (Objects.nonNull(innerSelect) && Objects.nonNull(innerSelect.getPlainSelect())) {
                collectFromPlainSelect(innerSelect.getPlainSelect(), references);
            }
        }
    }

    private static Table normalizeTable(Table source) {
        Table table = new Table(normalizeTableName(source));
        table.setAlias(source.getAlias());
        return table;
    }

    private static String normalizeTableName(Table table) {
        String name = table.getName();
        if (!StringUtils.hasText(name)) {
            return "";
        }
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < name.length() - 1) {
            name = name.substring(dotIndex + 1);
        }
        return name.replace("`", "").toLowerCase(Locale.ROOT);
    }

    private static String aliasKey(Table table) {
        return Objects.nonNull(table.getAlias()) ? table.getAlias().getName() : "";
    }

    private static String aliasName(Table table) {
        return Objects.nonNull(table.getAlias()) ? table.getAlias().getName() : null;
    }

    private static Expression buildRequiredCondition(
        Table table,
        DataIsolationMeta meta,
        Long organId,
        Expression permissionExpr
    ) {
        Expression organExpr = buildOrganExpression(table, meta.getOrganIdColumnName(), organId);
        if (Objects.isNull(permissionExpr)) {
            return organExpr;
        }
        return new AndExpression(
            new ParenthesedExpressionList<>(organExpr),
            new ParenthesedExpressionList<>(permissionExpr)
        );
    }

    private static Expression buildOrganExpression(Table table, String columnName, Long organId) {
        StringBuilder column = new StringBuilder();
        if (Objects.nonNull(table.getAlias())) {
            column.append(table.getAlias().getName()).append(".");
        }
        Column colName = new Column(column.append(columnName).toString());
        return new EqualsTo(colName, new LongValue(organId));
    }

    private static String resolveFailureMessage(
        Expression userWhere,
        DataIsolationMeta meta,
        Long organId,
        Expression permissionExpr
    ) {
        Expression organExpr = buildOrganExpression(new Table(meta.getPermissionTableName()), meta.getOrganIdColumnName(), organId);
        if (!WhereExpressionCoverageChecker.covers(userWhere, organExpr)) {
            return "缺少 organ_id 租户过滤条件";
        }
        if (Objects.nonNull(permissionExpr)
            && !WhereExpressionCoverageChecker.covers(userWhere, permissionExpr)) {
            return "缺少数据权限过滤条件";
        }
        return "未满足数据权限隔离条件";
    }
}
