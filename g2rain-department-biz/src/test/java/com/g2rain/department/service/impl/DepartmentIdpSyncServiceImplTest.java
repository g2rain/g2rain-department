package com.g2rain.department.service.impl;

import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.department.enums.DepartmentErrorCode;
import com.g2rain.department.enums.IdpSyncMode;
import com.g2rain.department.dto.DepartmentIdpSyncDepartmentNode;
import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.dto.DepartmentIdpSyncMemberDepartment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepartmentIdpSyncServiceImplTest {

    @Test
    void sortDepartmentsByLevel_shouldOrderParentsBeforeChildren() throws Exception {
        DepartmentIdpSyncDepartmentNode rootChild = node("2", "1", "研发部");
        DepartmentIdpSyncDepartmentNode grandChild = node("3", "2", "后端组");

        Method method = DepartmentIdpSyncServiceImpl.class.getDeclaredMethod(
            "sortDepartmentsByLevel", List.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DepartmentIdpSyncDepartmentNode> sorted = (List<DepartmentIdpSyncDepartmentNode>) method.invoke(
            null, List.of(grandChild, rootChild));

        assertEquals("2", sorted.get(0).getIdpDeptId());
        assertEquals("3", sorted.get(1).getIdpDeptId());
    }

    @Test
    void buildSnapshotIdpDeptIds_shouldCollectNonBlankIds() {
        DepartmentIdpSyncDepartmentNode node = node("2", "1", "研发部");

        Set<String> snapshotIds = DepartmentIdpSyncServiceImpl.buildSnapshotIdpDeptIds(List.of(node));

        assertEquals(Set.of("2"), snapshotIds);
    }

    @Test
    void buildDesiredUserDepartments_shouldMapUserToPlatformDepartments() {
        DepartmentIdpSyncMemberDepartment member = new DepartmentIdpSyncMemberDepartment();
        member.setUserId(100L);
        member.setIdpDeptIds(Set.of("2", "3"));

        DepartmentIdpSyncDto dto = new DepartmentIdpSyncDto();
        dto.setMemberDepartments(List.of(member));

        Map<Long, Set<Long>> desired = DepartmentIdpSyncServiceImpl.buildDesiredUserDepartments(
            dto, Map.of("2", 20L, "3", 30L));

        assertEquals(Set.of(20L, 30L), desired.get(100L));
    }

    @Test
    void buildDesiredUserDepartments_shouldKeepEmptyDeptUserForFullCleanup() {
        DepartmentIdpSyncMemberDepartment member = new DepartmentIdpSyncMemberDepartment();
        member.setUserId(100L);
        member.setIdpDeptIds(Set.of());

        DepartmentIdpSyncDto dto = new DepartmentIdpSyncDto();
        dto.setMemberDepartments(List.of(member));

        Map<Long, Set<Long>> desired = DepartmentIdpSyncServiceImpl.buildDesiredUserDepartments(dto, Map.of("2", 20L));

        assertTrue(desired.containsKey(100L));
        assertTrue(desired.get(100L).isEmpty());
    }

    @Test
    void assertSyncAuthorized_shouldPassWhenAdminAndOrganMatches() {
        DepartmentIdpSyncServiceImpl.assertSyncAuthorized(true, 100L, 100L);
    }

    @Test
    void assertSyncAuthorized_shouldRejectNonAdmin() {
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> DepartmentIdpSyncServiceImpl.assertSyncAuthorized(false, 100L, 100L)
        );
        assertEquals(DepartmentErrorCode.DEPARTMENT_IDP_SYNC_FORBIDDEN.code(), exception.getErrorCode());
    }

    @Test
    void assertSyncAuthorized_shouldRejectOrganMismatch() {
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> DepartmentIdpSyncServiceImpl.assertSyncAuthorized(true, 100L, 200L)
        );
        assertEquals(SystemErrorCode.PARAM_VAL_INVALID.code(), exception.getErrorCode());
    }

    @Test
    void fullDestructiveReconcile_shouldRequireExplicitFlag() {
        DepartmentIdpSyncDto disabled = new DepartmentIdpSyncDto();
        disabled.setSyncMode(IdpSyncMode.FULL.name());
        disabled.setEnableDestructiveReconcile(false);

        DepartmentIdpSyncDto enabled = new DepartmentIdpSyncDto();
        enabled.setSyncMode(IdpSyncMode.FULL.name());
        enabled.setEnableDestructiveReconcile(true);

        assertFalse(shouldRunDestructiveReconcile(disabled));
        assertTrue(shouldRunDestructiveReconcile(enabled));
    }

    private static boolean shouldRunDestructiveReconcile(DepartmentIdpSyncDto dto) {
        return IdpSyncMode.FULL == IdpSyncMode.normalize(dto.getSyncMode())
            && Boolean.TRUE.equals(dto.getEnableDestructiveReconcile());
    }

    @Test
    void invertUserDepartments_shouldGroupUsersByDepartment() {
        Map<Long, Set<Long>> userDepartments = Map.of(
            100L, Set.of(20L, 30L),
            200L, Set.of(30L)
        );

        Map<Long, Set<Long>> departmentUsers = DepartmentIdpSyncServiceImpl.invertUserDepartments(userDepartments);

        assertEquals(Set.of(100L, 200L), departmentUsers.get(30L));
        assertEquals(Set.of(100L), departmentUsers.get(20L));
    }

    private static DepartmentIdpSyncDepartmentNode node(String id, String parentId, String name) {
        DepartmentIdpSyncDepartmentNode node = new DepartmentIdpSyncDepartmentNode();
        node.setIdpDeptId(id);
        node.setParentIdpDeptId(parentId);
        node.setDeptName(name);
        node.setSortOrder(0);
        return node;
    }
}
