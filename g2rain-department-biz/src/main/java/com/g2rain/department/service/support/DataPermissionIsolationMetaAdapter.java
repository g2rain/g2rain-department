package com.g2rain.department.service.support;

import com.g2rain.department.dao.po.DataPermissionFieldPo;
import com.g2rain.department.dao.po.DataPermissionModelPo;
import com.g2rain.data.isolation.model.DataIsolationMeta;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 将权限模型与字段配置转换为隔离元信息。
 */
public final class DataPermissionIsolationMetaAdapter {

    private static final String ORGAN_ID = "organ_id";
    private static final String DEPT_PATH = "dept_path";
    private static final String USER_ID = "user_id";
    private static final String OWNER_USER_ID = "owner_user_id";

    private DataPermissionIsolationMetaAdapter() {
    }

    public static DataIsolationMeta toMeta(DataPermissionModelPo model, List<DataPermissionFieldPo> fields) {
        DataIsolationMeta meta = new DataIsolationMeta();
        meta.setPermissionTableName(model.getTableName());
        meta.setOrganIdColumnName(resolveFieldName(fields, ORGAN_ID, ORGAN_ID));

        boolean enableUser = !Boolean.FALSE.equals(model.getEnableUserScope());
        boolean enableDept = !Boolean.FALSE.equals(model.getEnableDeptScope());

        if (enableUser) {
            meta.setUserIdColumnName(resolveUserColumn(fields));
        } else {
            meta.setUserIdColumnName("");
        }

        if (enableDept) {
            meta.setDeptPathColumnName(resolveFieldName(fields, DEPT_PATH, ""));
        } else {
            meta.setDeptPathColumnName("");
        }
        return meta;
    }

    private static String resolveUserColumn(List<DataPermissionFieldPo> fields) {
        String owner = resolveFieldName(fields, OWNER_USER_ID, "");
        if (StringUtils.hasText(owner)) {
            return owner;
        }
        return resolveFieldName(fields, USER_ID, "");
    }

    private static String resolveFieldName(List<DataPermissionFieldPo> fields, String target, String defaultValue) {
        if (fields == null || fields.isEmpty()) {
            return defaultValue;
        }
        for (DataPermissionFieldPo field : fields) {
            if (Objects.nonNull(field.getFieldName())
                && target.equalsIgnoreCase(field.getFieldName().trim())) {
                return field.getFieldName().trim();
            }
        }
        return defaultValue;
    }
}
