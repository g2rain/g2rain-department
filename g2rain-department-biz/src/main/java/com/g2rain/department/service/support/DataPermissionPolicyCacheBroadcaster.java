package com.g2rain.department.service.support;

import com.g2rain.common.syncer.EventPublisherHub;
import com.g2rain.common.utils.Collections;
import com.g2rain.data.isolation.model.PermissionPolicyScope;
import com.g2rain.department.dao.DataPermissionGroupUserRelationDao;
import com.g2rain.department.dao.DataPermissionMetaDao;
import com.g2rain.department.dao.DataPermissionModelDao;
import com.g2rain.department.dao.DepartmentUserRelationDao;
import com.g2rain.department.dao.po.DataPermissionGroupUserRelationPo;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dao.po.DataPermissionModelPo;
import com.g2rain.department.dao.po.DataPermissionOtherPo;
import com.g2rain.department.dao.po.DepartmentPo;
import com.g2rain.department.dao.po.DepartmentUserRelationPo;
import com.g2rain.department.dto.DataPermissionGroupUserRelationSelectDto;
import com.g2rain.department.utils.DeptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 数据权限策略缓存失效广播。
 */
@Component
@RequiredArgsConstructor
public class DataPermissionPolicyCacheBroadcaster {

    private final EventPublisherHub eventPublisherHub;

    private final DataPermissionMetaDao dataPermissionMetaDao;

    private final DataPermissionModelDao dataPermissionModelDao;

    private final DataPermissionGroupUserRelationDao dataPermissionGroupUserRelationDao;

    private final DepartmentUserRelationDao departmentUserRelationDao;

    public void broadcastByMeta(DataPermissionMetaPo meta) {
        if (Objects.isNull(meta)) {
            return;
        }

        DataPermissionModelPo model = modelOf(meta.getModelId());
        if (Objects.isNull(model)) {
            return;
        }

        publish(PermissionPolicyScope.organModel(meta.getOrganId(), model.getModuleCode(), model.getTableName()));
    }

    public void broadcastMetaChange(DataPermissionMetaPo before, DataPermissionMetaPo after) {
        if (Objects.isNull(after)) {
            return;
        }

        if (Objects.nonNull(before) && !Objects.equals(before.getModelId(), after.getModelId())) {
            broadcastByMeta(before);
        }

        broadcastByMeta(after);
    }

    public void broadcastByOther(DataPermissionOtherPo other) {
        if (Objects.isNull(other)) {
            return;
        }

        DataPermissionMetaPo meta = dataPermissionMetaDao.selectById(other.getMetaId());
        if (Objects.isNull(meta)) {
            return;
        }

        DataPermissionModelPo model = modelOf(meta.getModelId());
        if (Objects.isNull(model)) {
            return;
        }

        DataPermissionGroupUserRelationSelectDto selectDto = new DataPermissionGroupUserRelationSelectDto();
        selectDto.setOrganId(meta.getOrganId());
        selectDto.setGroupId(other.getGroupId());
        List<DataPermissionGroupUserRelationPo> relations = dataPermissionGroupUserRelationDao.selectList(selectDto);
        if (Collections.isEmpty(relations)) {
            return;
        }

        for (DataPermissionGroupUserRelationPo relation : relations) {
            broadcastExact(meta.getOrganId(), relation.getUserId(), model.getModuleCode(), model.getTableName());
        }
    }

    public void broadcastOtherChange(DataPermissionOtherPo before, DataPermissionOtherPo after) {
        if (Objects.isNull(after)) {
            return;
        }

        if (Objects.nonNull(before)
            && (!Objects.equals(before.getGroupId(), after.getGroupId())
            || !Objects.equals(before.getMetaId(), after.getMetaId()))) {
            broadcastByOther(before);
        }

        broadcastByOther(after);
    }

    public void broadcastByGroupId(Long organId, Long groupId) {
        if (Objects.isNull(organId) || Objects.isNull(groupId)) {
            return;
        }

        DataPermissionGroupUserRelationSelectDto selectDto = new DataPermissionGroupUserRelationSelectDto();
        selectDto.setOrganId(organId);
        selectDto.setGroupId(groupId);
        List<DataPermissionGroupUserRelationPo> relations = dataPermissionGroupUserRelationDao.selectList(selectDto);
        if (Collections.isEmpty(relations)) {
            return;
        }

        for (DataPermissionGroupUserRelationPo relation : relations) {
            broadcastOrganUser(relation.getOrganId(), relation.getUserId());
        }
    }

    public void broadcastGroupUserRelationChange(
        DataPermissionGroupUserRelationPo before,
        DataPermissionGroupUserRelationPo after
    ) {
        if (Objects.isNull(after)) {
            return;
        }

        if (Objects.nonNull(before)
            && (!Objects.equals(before.getUserId(), after.getUserId())
            || !Objects.equals(before.getGroupId(), after.getGroupId()))) {
            broadcastOrganUser(before.getOrganId(), before.getUserId());
        }

        broadcastOrganUser(after.getOrganId(), after.getUserId());
    }

    public void broadcastOrganUser(Long organId, Long userId) {
        if (Objects.isNull(organId) || Objects.isNull(userId)) {
            return;
        }

        List<String> deptPaths = departmentUserRelationDao.selectDeptPaths(organId, userId);
        if (Collections.isEmpty(deptPaths)) {
            return;
        }

        publish(PermissionPolicyScope.organUser(organId, userId, String.join(",", deptPaths)));
    }

    public void broadcastDepartmentUserRelationChange(DepartmentUserRelationPo before, DepartmentUserRelationPo after) {
        if (Objects.isNull(after)) {
            return;
        }

        if (Objects.nonNull(before)
            && (!Objects.equals(before.getUserId(), after.getUserId())
            || !Objects.equals(before.getDepartmentId(), after.getDepartmentId()))) {
            broadcastOrganUser(before.getOrganId(), before.getUserId());
        }

        broadcastOrganUser(after.getOrganId(), after.getUserId());
    }

    public void broadcastDepartmentLeaderChange(DepartmentPo before, DepartmentPo after) {
        if (!DataPermissionPolicyChangeDetector.departmentLeaderAffecting(before, after)) {
            return;
        }

        if (Objects.nonNull(before) && Objects.nonNull(before.getLeaderUserId())) {
            broadcastOrganUser(before.getOrganId(), before.getLeaderUserId());
        }

        if (Objects.nonNull(after) && Objects.nonNull(after.getLeaderUserId())
            && !Objects.equals(before == null ? null : before.getLeaderUserId(), after.getLeaderUserId())) {
            broadcastOrganUser(after.getOrganId(), after.getLeaderUserId());
        }
    }

    private void broadcastExact(Long organId, Long userId, String moduleCode, String tableName) {
        List<String> deptPaths = departmentUserRelationDao.selectDeptPaths(organId, userId);
        if (Collections.isEmpty(deptPaths)) {
            return;
        }

        publish(PermissionPolicyScope.exact(organId, userId, String.join(",", deptPaths), moduleCode, tableName));
    }

    private DataPermissionModelPo modelOf(Long modelId) {
        if (Objects.isNull(modelId)) {
            return null;
        }

        return dataPermissionModelDao.selectById(modelId);
    }

    private void publish(PermissionPolicyScope scope) {
        eventPublisherHub.sendUpdate(
            DeptUtils.SYNC_OUTPUT_BINDING,
            DeptUtils.DATA_PERMISSION_POLICY_SOURCE,
            scope
        );
    }
}
