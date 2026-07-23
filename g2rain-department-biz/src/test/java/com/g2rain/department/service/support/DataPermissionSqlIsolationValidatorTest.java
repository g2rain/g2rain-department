package com.g2rain.department.service.support;

import com.g2rain.department.vo.DataPermissionSqlValidateVo;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataPermissionSqlIsolationValidatorTest {

    @Test
    void extractTableReferences_shouldCollectJoinTables() throws JSQLParserException {
        List<DataPermissionSqlIsolationValidator.SqlTableReference> references =
            DataPermissionSqlIsolationValidator.extractTableReferences(
                "SELECT * FROM `order` o JOIN order_item oi ON o.id = oi.order_id"
            );

        assertEquals(2, references.size());
        assertEquals("order", normalizeName(references.get(0).table()));
        assertEquals("order_item", normalizeName(references.get(1).table()));
    }

    @Test
    void validate_shouldMarkUnconfiguredTableAsSkipped() {
        DataPermissionSqlValidateVo result = DataPermissionSqlIsolationValidator.validate(
            "SELECT * FROM dictionary d WHERE d.organ_id = 100",
            "demo",
            100L,
            "00010001",
            _ -> null
        );

        assertTrue(result.getPassed());
        assertEquals("无可校验的数据权限表", result.getMessage());
        assertEquals(1, result.getTables().size());
        assertTrue(result.getTables().getFirst().getSkipped());
    }

    @Test
    void validate_shouldFailWhenOrganFilterMissing() {
        DataPermissionIsolationContext context = buildOrderContext();

        DataPermissionSqlValidateVo result = DataPermissionSqlIsolationValidator.validate(
            "SELECT * FROM `order` o WHERE o.owner_user_id = 1001",
            "demo",
            100L,
            "00010001",
            tableName -> "order".equals(tableName) ? context : null
        );

        assertFalse(result.getPassed());
        assertEquals(1, result.getTables().size());
        assertFalse(result.getTables().getFirst().getSkipped());
        assertFalse(result.getTables().getFirst().getPassed());
        assertTrue(result.getTables().getFirst().getMessage().contains("organ_id"));
    }

    @Test
    void validate_shouldPassWhenOrganAndPermissionPresent() {
        DataPermissionIsolationContext context = buildOrderContext();

        DataPermissionSqlValidateVo result = DataPermissionSqlIsolationValidator.validate(
            "SELECT * FROM `order` o WHERE o.organ_id = 100 AND o.owner_user_id = 1001",
            "demo",
            100L,
            "00010001",
            tableName -> "order".equals(tableName) ? context : null
        );

        assertTrue(result.getPassed());
        assertEquals(1, result.getTables().size());
        assertTrue(result.getTables().getFirst().getPassed());
    }

    @Test
    void validate_shouldFailWhenSqlCannotBeParsed() {
        DataPermissionSqlValidateVo result = DataPermissionSqlIsolationValidator.validate(
            "NOT A SELECT",
            "demo",
            100L,
            "00010001",
            _ -> null
        );

        assertFalse(result.getPassed());
        assertEquals("无法解析 SQL", result.getMessage());
    }

    private static DataPermissionIsolationContext buildOrderContext() {
        com.g2rain.department.dao.po.DataPermissionModelPo model = new com.g2rain.department.dao.po.DataPermissionModelPo();
        model.setTableName("order");
        model.setEnableUserScope(true);
        model.setEnableDeptScope(false);

        com.g2rain.department.dao.po.DataPermissionFieldPo ownerField = new com.g2rain.department.dao.po.DataPermissionFieldPo();
        ownerField.setFieldName("owner_user_id");
        com.g2rain.department.dao.po.DataPermissionFieldPo organField = new com.g2rain.department.dao.po.DataPermissionFieldPo();
        organField.setFieldName("organ_id");

        DataPermissionIsolationContext context = new DataPermissionIsolationContext();
        context.setModelPo(model);
        context.setIsolationMeta(DataPermissionIsolationMetaAdapter.toMeta(
            model, List.of(ownerField, organField)
        ));
        com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult policy =
            new com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult();
        policy.setGroupRead(false);
        policy.setOtherRead(false);
        context.setPolicy(policy);
        return context;
    }

    private static String normalizeName(Table table) {
        return table.getName().replace("`", "").toLowerCase();
    }
}
