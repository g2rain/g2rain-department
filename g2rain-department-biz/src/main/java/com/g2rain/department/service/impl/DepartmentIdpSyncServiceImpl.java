package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.utils.Strings;
import com.g2rain.department.dao.DepartmentDao;
import com.g2rain.department.dao.DepartmentIdpMappingDao;
import com.g2rain.department.dao.DepartmentUserRelationDao;
import com.g2rain.department.dao.po.DepartmentIdpMappingPo;
import com.g2rain.department.dao.po.DepartmentPo;
import com.g2rain.department.dao.po.DepartmentUserRelationPo;
import com.g2rain.department.dto.DepartmentAssignUsersDto;
import com.g2rain.department.dto.DepartmentDto;
import com.g2rain.department.dto.DepartmentIdpSyncDepartmentNode;
import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.dto.DepartmentIdpSyncMemberDepartment;
import com.g2rain.department.dto.DepartmentUserRelationSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.enums.IdpSyncMode;
import com.g2rain.department.service.DepartmentIdpSyncService;
import com.g2rain.department.service.DepartmentService;
import com.g2rain.department.service.DepartmentUserRelationService;
import com.g2rain.department.utils.DeptUtils;
import com.g2rain.department.vo.DepartmentIdpSyncResultVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 部门 IdP 同步服务实现。
 */
@Service(value = "departmentIdpSyncServiceImpl")
public class DepartmentIdpSyncServiceImpl implements DepartmentIdpSyncService {

    private static final String ROOT_IDP_DEPT_ID = "1";

    @Resource(name = "departmentIdpMappingDao")
    private DepartmentIdpMappingDao departmentIdpMappingDao;

    @Resource(name = "departmentDao")
    private DepartmentDao departmentDao;

    @Resource(name = "departmentServiceImpl")
    private DepartmentService departmentService;

    @Resource(name = "departmentUserRelationServiceImpl")
    private DepartmentUserRelationService departmentUserRelationService;

    @Resource(name = "departmentUserRelationDao")
    private DepartmentUserRelationDao departmentUserRelationDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public DepartmentIdpSyncResultVo sync(DepartmentIdpSyncDto dto) {
        DepartmentIdpSyncResultVo result = new DepartmentIdpSyncResultVo();
        IdpSyncMode syncMode = IdpSyncMode.normalize(dto.getSyncMode());
        Map<String, Long> idpDeptToPlatform = loadExistingMappings(dto.getOrganId(), dto.getIdpType());
        Set<String> snapshotIdpDeptIds = buildSnapshotIdpDeptIds(dto.getDepartments());

        if (!Collections.isEmpty(dto.getDepartments())) {
            List<DepartmentIdpSyncDepartmentNode> sortedDepartments = sortDepartmentsByLevel(dto.getDepartments());
            for (DepartmentIdpSyncDepartmentNode node : sortedDepartments) {
                if (ROOT_IDP_DEPT_ID.equals(node.getIdpDeptId())) {
                    continue;
                }
                syncDepartment(dto.getOrganId(), dto.getIdpType(), node, idpDeptToPlatform, result);
            }
        }

        if (syncMode == IdpSyncMode.FULL) {
            reconcileOffboardedDepartments(idpDeptToPlatform, snapshotIdpDeptIds, result);
        }

        syncMemberDepartments(dto, syncMode, idpDeptToPlatform, result);
        return result;
    }

    private Map<String, Long> loadExistingMappings(Long organId, String idpType) {
        Map<String, Long> mappings = new HashMap<>();
        departmentIdpMappingDao.selectByOrganAndIdpType(organId, idpType)
            .forEach(item -> mappings.put(item.getIdpDeptId(), item.getDepartmentId()));
        return mappings;
    }

    private void syncDepartment(
        Long organId,
        String idpType,
        DepartmentIdpSyncDepartmentNode node,
        Map<String, Long> idpDeptToPlatform,
        DepartmentIdpSyncResultVo result
    ) {
        Long existingDepartmentId = idpDeptToPlatform.get(node.getIdpDeptId());
        if (existingDepartmentId != null) {
            updateDepartmentIfNeeded(existingDepartmentId, node, result);
            return;
        }

        Long parentPlatformId = resolveParentPlatformId(node.getParentIdpDeptId(), idpDeptToPlatform);
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setOrganId(organId);
        departmentDto.setParentId(parentPlatformId);
        departmentDto.setDeptName(node.getDeptName());
        departmentDto.setSortOrder(Objects.requireNonNullElse(node.getSortOrder(), 0));
        Long departmentId = departmentService.save(departmentDto);

        DepartmentIdpMappingPo mapping = new DepartmentIdpMappingPo();
        mapping.setId(idGenerator.generateId());
        LocalDateTime now = Moments.now();
        mapping.setCreateTime(now);
        mapping.setUpdateTime(now);
        mapping.setOrganId(organId);
        mapping.setIdpType(idpType);
        mapping.setIdpDeptId(node.getIdpDeptId());
        mapping.setDepartmentId(departmentId);
        int inserted = departmentIdpMappingDao.insert(mapping);
        Asserts.greaterThan(inserted, 0, SystemErrorCode.CREATE_DATA_ERROR);
        idpDeptToPlatform.put(node.getIdpDeptId(), departmentId);
        result.setDepartmentsCreated(result.getDepartmentsCreated() + 1);
    }

    private void updateDepartmentIfNeeded(
        Long departmentId,
        DepartmentIdpSyncDepartmentNode node,
        DepartmentIdpSyncResultVo result
    ) {
        DepartmentPo existing = departmentDao.selectById(departmentId);
        if (existing == null) {
            return;
        }
        boolean changed = false;
        DepartmentPo update = new DepartmentPo();
        update.setId(departmentId);
        update.setUpdateTime(Moments.now());
        if (Strings.isNotBlank(node.getDeptName()) && !Objects.equals(existing.getDeptName(), node.getDeptName())) {
            update.setDeptName(node.getDeptName());
            changed = true;
        }
        if (node.getSortOrder() != null && !Objects.equals(existing.getSortOrder(), node.getSortOrder())) {
            update.setSortOrder(node.getSortOrder());
            changed = true;
        }
        if (CommonStatus.INACTIVE.name().equals(existing.getStatus())) {
            update.setStatus(CommonStatus.ACTIVE.name());
            changed = true;
        }
        if (!changed) {
            return;
        }
        int updated = departmentDao.update(update);
        Asserts.greaterThan(updated, 0, SystemErrorCode.UPDATE_DATA_ERROR, departmentId);
        result.setDepartmentsUpdated(result.getDepartmentsUpdated() + 1);
    }

    private void reconcileOffboardedDepartments(
        Map<String, Long> idpDeptToPlatform,
        Set<String> snapshotIdpDeptIds,
        DepartmentIdpSyncResultVo result
    ) {
        for (Map.Entry<String, Long> entry : idpDeptToPlatform.entrySet()) {
            String idpDeptId = entry.getKey();
            if (ROOT_IDP_DEPT_ID.equals(idpDeptId) || snapshotIdpDeptIds.contains(idpDeptId)) {
                continue;
            }
            disableDepartmentIfActive(entry.getValue(), result);
        }
    }

    private void disableDepartmentIfActive(Long departmentId, DepartmentIdpSyncResultVo result) {
        DepartmentPo existing = departmentDao.selectById(departmentId);
        if (existing == null || CommonStatus.INACTIVE.name().equals(existing.getStatus())) {
            return;
        }
        UpdateStatusDto statusDto = new UpdateStatusDto();
        statusDto.setStatus(CommonStatus.INACTIVE.name());
        if (departmentService.updateStatus(departmentId, statusDto) > 0) {
            result.setDepartmentsDisabled(result.getDepartmentsDisabled() + 1);
        }
    }

    private void syncMemberDepartments(
        DepartmentIdpSyncDto dto,
        IdpSyncMode syncMode,
        Map<String, Long> idpDeptToPlatform,
        DepartmentIdpSyncResultVo result
    ) {
        if (syncMode == IdpSyncMode.FULL) {
            syncMemberDepartmentsFull(dto, idpDeptToPlatform, result);
            return;
        }
        syncMemberDepartmentsIncremental(dto, idpDeptToPlatform, result);
    }

    private void syncMemberDepartmentsIncremental(
        DepartmentIdpSyncDto dto,
        Map<String, Long> idpDeptToPlatform,
        DepartmentIdpSyncResultVo result
    ) {
        if (Collections.isEmpty(dto.getMemberDepartments())) {
            return;
        }
        Map<Long, Set<Long>> departmentUsers = aggregateDepartmentUsers(dto, idpDeptToPlatform, false);
        assignUsersByDepartment(dto.getOrganId(), departmentUsers, result);
    }

    private void syncMemberDepartmentsFull(
        DepartmentIdpSyncDto dto,
        Map<String, Long> idpDeptToPlatform,
        DepartmentIdpSyncResultVo result
    ) {
        Set<Long> mappedDepartmentIds = new LinkedHashSet<>(idpDeptToPlatform.values());
        if (mappedDepartmentIds.isEmpty()) {
            return;
        }
        Map<Long, Set<Long>> desiredByUser = buildDesiredUserDepartments(dto, idpDeptToPlatform);
        removeStaleRelations(dto.getOrganId(), mappedDepartmentIds, desiredByUser, result);
        assignUsersByDepartment(dto.getOrganId(), desiredByUser, result);
    }

    private void removeStaleRelations(
        Long organId,
        Set<Long> mappedDepartmentIds,
        Map<Long, Set<Long>> desiredByUser,
        DepartmentIdpSyncResultVo result
    ) {
        DepartmentUserRelationSelectDto query = new DepartmentUserRelationSelectDto();
        query.setOrganId(organId);
        query.setDepartmentIds(mappedDepartmentIds);
        for (DepartmentUserRelationPo relation : departmentUserRelationDao.selectListWithoutIsolation(query)) {
            Set<Long> desiredDepartments = desiredByUser.get(relation.getUserId());
            if (desiredDepartments != null && desiredDepartments.contains(relation.getDepartmentId())) {
                continue;
            }
            if (departmentUserRelationService.delete(relation.getId()) > 0) {
                result.setRelationsRemoved(result.getRelationsRemoved() + 1);
            }
        }
    }

    private void assignUsersByDepartment(
        Long organId,
        Map<Long, Set<Long>> userDepartments,
        DepartmentIdpSyncResultVo result
    ) {
        Map<Long, Set<Long>> departmentUsers = invertUserDepartments(userDepartments);
        for (Map.Entry<Long, Set<Long>> entry : departmentUsers.entrySet()) {
            DepartmentAssignUsersDto assignDto = new DepartmentAssignUsersDto();
            assignDto.setOrganId(organId);
            assignDto.setDepartmentId(entry.getKey());
            assignDto.setUserIds(entry.getValue());
            int inserted = departmentUserRelationService.addUsers(assignDto);
            result.setRelationsCreated(result.getRelationsCreated() + inserted);
        }
    }

    static Map<Long, Set<Long>> buildDesiredUserDepartments(
        DepartmentIdpSyncDto dto,
        Map<String, Long> idpDeptToPlatform
    ) {
        Map<Long, Set<Long>> desiredByUser = new LinkedHashMap<>();
        if (Collections.isEmpty(dto.getMemberDepartments())) {
            return desiredByUser;
        }
        for (DepartmentIdpSyncMemberDepartment member : dto.getMemberDepartments()) {
            if (member.getUserId() == null) {
                continue;
            }
            Set<Long> departmentIds = desiredByUser.computeIfAbsent(member.getUserId(), ignored -> new LinkedHashSet<>());
            if (Collections.isEmpty(member.getIdpDeptIds())) {
                continue;
            }
            for (String idpDeptId : member.getIdpDeptIds()) {
                if (ROOT_IDP_DEPT_ID.equals(idpDeptId)) {
                    continue;
                }
                Long departmentId = idpDeptToPlatform.get(idpDeptId);
                if (departmentId != null) {
                    departmentIds.add(departmentId);
                }
            }
        }
        return desiredByUser;
    }

    private static Map<Long, Set<Long>> aggregateDepartmentUsers(
        DepartmentIdpSyncDto dto,
        Map<String, Long> idpDeptToPlatform,
        boolean includeEmptyDeptMembers
    ) {
        Map<Long, Set<Long>> departmentUsers = new LinkedHashMap<>();
        if (Collections.isEmpty(dto.getMemberDepartments())) {
            return departmentUsers;
        }
        for (DepartmentIdpSyncMemberDepartment member : dto.getMemberDepartments()) {
            if (member.getUserId() == null) {
                continue;
            }
            if (!includeEmptyDeptMembers && Collections.isEmpty(member.getIdpDeptIds())) {
                continue;
            }
            for (String idpDeptId : member.getIdpDeptIds()) {
                if (ROOT_IDP_DEPT_ID.equals(idpDeptId)) {
                    continue;
                }
                Long departmentId = idpDeptToPlatform.get(idpDeptId);
                if (departmentId == null) {
                    continue;
                }
                departmentUsers.computeIfAbsent(departmentId, ignored -> new LinkedHashSet<>())
                    .add(member.getUserId());
            }
        }
        return departmentUsers;
    }

    static Map<Long, Set<Long>> invertUserDepartments(Map<Long, Set<Long>> userDepartments) {
        Map<Long, Set<Long>> departmentUsers = new LinkedHashMap<>();
        for (Map.Entry<Long, Set<Long>> entry : userDepartments.entrySet()) {
            for (Long departmentId : entry.getValue()) {
                departmentUsers.computeIfAbsent(departmentId, ignored -> new LinkedHashSet<>())
                    .add(entry.getKey());
            }
        }
        return departmentUsers;
    }

    static Set<String> buildSnapshotIdpDeptIds(List<DepartmentIdpSyncDepartmentNode> departments) {
        Set<String> snapshotIds = new HashSet<>();
        if (Collections.isEmpty(departments)) {
            return snapshotIds;
        }
        for (DepartmentIdpSyncDepartmentNode department : departments) {
            if (Strings.isNotBlank(department.getIdpDeptId())) {
                snapshotIds.add(department.getIdpDeptId().trim());
            }
        }
        return snapshotIds;
    }

    private static Long resolveParentPlatformId(String parentIdpDeptId, Map<String, Long> idpDeptToPlatform) {
        if (Strings.isBlank(parentIdpDeptId) || ROOT_IDP_DEPT_ID.equals(parentIdpDeptId)) {
            return DeptUtils.ROOT_PARENT_ID;
        }
        return idpDeptToPlatform.getOrDefault(parentIdpDeptId, DeptUtils.ROOT_PARENT_ID);
    }

    private static List<DepartmentIdpSyncDepartmentNode> sortDepartmentsByLevel(
        List<DepartmentIdpSyncDepartmentNode> departments
    ) {
        if (Collections.isEmpty(departments)) {
            return List.of();
        }
        Map<String, DepartmentIdpSyncDepartmentNode> nodeById = new LinkedHashMap<>();
        for (DepartmentIdpSyncDepartmentNode node : departments) {
            if (Strings.isNotBlank(node.getIdpDeptId())) {
                nodeById.put(node.getIdpDeptId(), node);
            }
        }
        Map<String, Integer> depthCache = new HashMap<>();
        List<DepartmentIdpSyncDepartmentNode> sorted = new ArrayList<>(nodeById.values());
        sorted.sort(Comparator.comparingInt(node -> depthOf(node.getIdpDeptId(), nodeById, depthCache)));
        return sorted;
    }

    private static int depthOf(
        String idpDeptId,
        Map<String, DepartmentIdpSyncDepartmentNode> nodeById,
        Map<String, Integer> depthCache
    ) {
        if (depthCache.containsKey(idpDeptId)) {
            return depthCache.get(idpDeptId);
        }
        DepartmentIdpSyncDepartmentNode node = nodeById.get(idpDeptId);
        if (node == null || Strings.isBlank(node.getParentIdpDeptId()) || ROOT_IDP_DEPT_ID.equals(node.getParentIdpDeptId())) {
            depthCache.put(idpDeptId, 0);
            return 0;
        }
        int depth = depthOf(node.getParentIdpDeptId(), nodeById, depthCache) + 1;
        depthCache.put(idpDeptId, depth);
        return depth;
    }
}
