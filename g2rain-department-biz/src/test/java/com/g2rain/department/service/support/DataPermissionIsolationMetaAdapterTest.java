package com.g2rain.department.service.support;

import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.department.dao.po.DataPermissionFieldPo;
import com.g2rain.department.dao.po.DataPermissionModelPo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataPermissionIsolationMetaAdapterTest {

    @Test
    void toMeta_shouldDisableUserAndDeptColumnsWhenScopeOff() {
        DataPermissionModelPo model = new DataPermissionModelPo();
        model.setTableName("order");
        model.setEnableUserScope(false);
        model.setEnableDeptScope(false);

        List<DataPermissionFieldPo> fields = List.of(
            field("organ_id"),
            field("owner_user_id"),
            field("dept_path")
        );

        DataIsolationMeta meta = DataPermissionIsolationMetaAdapter.toMeta(model, fields);

        assertEquals("order", meta.getPermissionTableName());
        assertEquals("organ_id", meta.getOrganIdColumnName());
        assertFalse(meta.hasUserColumn());
        assertFalse(meta.hasDeptColumn());
    }

    @Test
    void toMeta_shouldPreferOwnerUserIdColumn() {
        DataPermissionModelPo model = new DataPermissionModelPo();
        model.setTableName("order");
        model.setEnableUserScope(true);
        model.setEnableDeptScope(true);

        DataIsolationMeta meta = DataPermissionIsolationMetaAdapter.toMeta(
            model,
            List.of(field("owner_user_id"), field("dept_path"))
        );

        assertTrue(meta.hasUserColumn());
        assertEquals("owner_user_id", meta.getUserIdColumnName());
        assertEquals("dept_path", meta.getDeptPathColumnName());
    }

    private static DataPermissionFieldPo field(String name) {
        DataPermissionFieldPo field = new DataPermissionFieldPo();
        field.setFieldName(name);
        return field;
    }
}
