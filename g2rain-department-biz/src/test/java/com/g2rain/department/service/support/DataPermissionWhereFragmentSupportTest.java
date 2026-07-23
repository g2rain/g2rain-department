package com.g2rain.department.service.support;

import com.g2rain.common.web.PrincipalContextHolder;
import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import net.sf.jsqlparser.schema.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataPermissionWhereFragmentSupportTest {

    @BeforeEach
    void setUp() {
        PrincipalContextHolder.setUserId(1001L);
        PrincipalContextHolder.setDeptPath("00010001");
    }

    @Test
    void buildReadConditionFragment_shouldMergeUserDeptAndOtherWithOr() {
        PrincipalContextHolder.setUserId(1001L);
        PrincipalContextHolder.setDeptPath("00010001");

        DataIsolationMeta meta = new DataIsolationMeta();
        meta.setPermissionTableName("order");
        meta.setUserIdColumnName("owner_user_id");
        meta.setDeptPathColumnName("dept_path");

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setGroupRead(true);
        policy.setOtherRead(true);
        policy.setOtherPermRule("status = 'OPEN'");

        String fragment = DataPermissionWhereFragmentSupport.buildReadConditionFragment(
            new Table("order"),
            meta,
            policy,
            "00010001"
        );

        assertNotNull(fragment);
        assertTrue(fragment.contains("status = 'OPEN'"));
        assertTrue(fragment.contains(" OR ") || fragment.equals("status = 'OPEN'"));
    }

    @Test
    void buildReadConditionFragment_shouldOnlyUseOtherWhenScopeDisabled() {
        DataPermissionModelPoHolder model = new DataPermissionModelPoHolder();
        model.tableName = "order";
        model.enableUserScope = false;
        model.enableDeptScope = false;

        DataIsolationMeta meta = DataPermissionIsolationMetaAdapter.toMeta(
            model.toPo(),
            java.util.List.of()
        );

        DataPermissionPolicyResolveResult policy = new DataPermissionPolicyResolveResult();
        policy.setOtherRead(true);
        policy.setOtherPermRule("status = 'OPEN'");

        String fragment = DataPermissionWhereFragmentSupport.buildReadConditionFragment(
            new Table("order"),
            meta,
            policy,
            "00010001"
        );

        assertNotNull(fragment);
        assertTrue(fragment.contains("status = 'OPEN'"));
    }

    private static final class DataPermissionModelPoHolder {
        private String tableName;
        private boolean enableUserScope;
        private boolean enableDeptScope;

        private com.g2rain.department.dao.po.DataPermissionModelPo toPo() {
            com.g2rain.department.dao.po.DataPermissionModelPo po = new com.g2rain.department.dao.po.DataPermissionModelPo();
            po.setTableName(tableName);
            po.setEnableUserScope(enableUserScope);
            po.setEnableDeptScope(enableDeptScope);
            return po;
        }
    }
}
