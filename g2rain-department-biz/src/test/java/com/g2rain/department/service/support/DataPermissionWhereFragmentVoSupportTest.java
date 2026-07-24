package com.g2rain.department.service.support;

import com.g2rain.common.web.PrincipalContextHolder;
import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import com.g2rain.department.enums.DepartmentErrorCode;
import com.g2rain.department.vo.DataPermissionWhereFragmentVo;
import net.sf.jsqlparser.schema.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataPermissionWhereFragmentVoSupportTest {

    @BeforeEach
    void setUp() {
        PrincipalContextHolder.setUserId(1001L);
        PrincipalContextHolder.setDeptPath("00010001");
    }

    @Test
    void enrich_shouldReturnFragmentWhenGroupReadAndInGroup() {
        DataIsolationMeta meta = new DataIsolationMeta();
        meta.setPermissionTableName("order");
        meta.setUserIdColumnName("owner_user_id");
        meta.setDeptPathColumnName("dept_path");

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setGroupRead(true);
        policy.setOtherRead(true);
        policy.setOtherPermRule("status = 'OPEN'");

        DataPermissionWhereFragmentVo vo = new DataPermissionWhereFragmentVo();
        DataPermissionWhereFragmentVoSupport.enrich(
            vo,
            policy,
            true,
            "00010001",
            new Table("order"),
            meta
        );

        assertTrue(vo.isHasQueryPermission());
        assertNotNull(vo.getWhereFragment());
        assertNull(vo.getMessage());
    }

    @Test
    void enrich_shouldDenyWhenGroupReadFalse() {
        DataIsolationMeta meta = new DataIsolationMeta();
        meta.setPermissionTableName("order");

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setGroupRead(false);
        policy.setOtherRead(true);
        policy.setOtherPermRule("status = 'OPEN'");

        DataPermissionWhereFragmentVo vo = new DataPermissionWhereFragmentVo();
        DataPermissionWhereFragmentVoSupport.enrich(
            vo,
            policy,
            true,
            "00010001",
            new Table("order"),
            meta
        );

        assertFalse(vo.isHasQueryPermission());
        assertNull(vo.getWhereFragment());
        assertEquals(
            DepartmentErrorCode.DATA_PERMISSION_TABLE_QUERY_FORBIDDEN.messageTemplate(),
            vo.getMessage()
        );
    }

    @Test
    void enrich_shouldAllowQueryWhenGatePassedButFragmentEmpty() {
        com.g2rain.department.dao.po.DataPermissionModelPo model = new com.g2rain.department.dao.po.DataPermissionModelPo();
        model.setTableName("order");
        model.setEnableUserScope(false);
        model.setEnableDeptScope(false);

        DataIsolationMeta meta = DataPermissionIsolationMetaAdapter.toMeta(model, java.util.List.of());

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setGroupRead(true);
        policy.setOtherRead(false);

        DataPermissionWhereFragmentVo vo = new DataPermissionWhereFragmentVo();
        DataPermissionWhereFragmentVoSupport.enrich(
            vo,
            policy,
            true,
            "00010001",
            new Table("order"),
            meta
        );

        assertTrue(vo.isHasQueryPermission());
        assertNull(vo.getWhereFragment());
        assertNull(vo.getMessage());
    }

    @Test
    void enrich_shouldDenyWhenNotInGroup() {
        DataIsolationMeta meta = new DataIsolationMeta();
        meta.setPermissionTableName("order");
        meta.setUserIdColumnName("owner_user_id");
        meta.setDeptPathColumnName("dept_path");

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setGroupRead(true);

        DataPermissionWhereFragmentVo vo = new DataPermissionWhereFragmentVo();
        DataPermissionWhereFragmentVoSupport.enrich(
            vo,
            policy,
            false,
            "00010001",
            new Table("order"),
            meta
        );

        assertFalse(vo.isHasQueryPermission());
        assertNull(vo.getWhereFragment());
        assertEquals(
            DepartmentErrorCode.DATA_PERMISSION_TABLE_QUERY_FORBIDDEN.messageTemplate(),
            vo.getMessage()
        );
    }
}
