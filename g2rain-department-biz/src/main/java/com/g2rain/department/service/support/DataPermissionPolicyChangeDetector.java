package com.g2rain.department.service.support;

import com.g2rain.department.dao.po.DataPermissionGroupPo;
import com.g2rain.department.dao.po.DataPermissionGroupUserRelationPo;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dao.po.DataPermissionOtherPo;
import com.g2rain.department.dao.po.DepartmentPo;

import java.util.Objects;

/**
 * 判断数据权限相关变更是否影响策略缓存。
 */
public final class DataPermissionPolicyChangeDetector {

    private DataPermissionPolicyChangeDetector() {
    }

    public static boolean metaAffecting(DataPermissionMetaPo before, DataPermissionMetaPo after) {
        if (Objects.isNull(before)) {
            return true;
        }

        return !Objects.equals(before.getPermissionMode(), after.getPermissionMode())
            || !Objects.equals(before.getModelId(), after.getModelId())
            || !Objects.equals(before.getStatus(), after.getStatus());
    }

    public static boolean otherAffecting(DataPermissionOtherPo before, DataPermissionOtherPo after) {
        if (Objects.isNull(before)) {
            return true;
        }

        return !Objects.equals(before.getPermissionMode(), after.getPermissionMode())
            || !Objects.equals(before.getPermissionRule(), after.getPermissionRule())
            || !Objects.equals(before.getMetaId(), after.getMetaId())
            || !Objects.equals(before.getGroupId(), after.getGroupId())
            || !Objects.equals(before.getStatus(), after.getStatus());
    }

    public static boolean groupAffecting(DataPermissionGroupPo before, DataPermissionGroupPo after) {
        if (Objects.isNull(before)) {
            return false;
        }

        return !Objects.equals(before.getDeptPath(), after.getDeptPath())
            || !Objects.equals(before.getStatus(), after.getStatus());
    }

    public static boolean groupUserRelationAffecting(
        DataPermissionGroupUserRelationPo before,
        DataPermissionGroupUserRelationPo after
    ) {
        if (Objects.isNull(before)) {
            return true;
        }

        return !Objects.equals(before.getUserId(), after.getUserId())
            || !Objects.equals(before.getGroupId(), after.getGroupId())
            || !Objects.equals(before.getStatus(), after.getStatus());
    }

    public static boolean departmentLeaderAffecting(DepartmentPo before, DepartmentPo after) {
        if (Objects.isNull(before)) {
            return Objects.nonNull(after.getLeaderUserId());
        }

        return !Objects.equals(before.getLeaderUserId(), after.getLeaderUserId())
            || !Objects.equals(before.getDeptPath(), after.getDeptPath())
            || !Objects.equals(before.getStatus(), after.getStatus());
    }
}
