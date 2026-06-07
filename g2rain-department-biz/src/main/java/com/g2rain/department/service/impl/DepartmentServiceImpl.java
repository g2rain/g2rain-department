package com.g2rain.department.service.impl;

import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DepartmentConverter;
import com.g2rain.department.dao.DepartmentDao;
import com.g2rain.department.dao.po.DepartmentPo;
import com.g2rain.department.dto.DepartmentDto;
import com.g2rain.department.dto.DepartmentSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.service.DepartmentService;
import com.g2rain.department.service.support.CommonStatusUpdater;
import com.g2rain.department.service.support.DataPermissionPolicyCacheBroadcaster;
import com.g2rain.department.utils.DeptUtils;
import com.g2rain.department.vo.DepartmentTreeVo;
import com.g2rain.department.vo.DepartmentVo;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * 部门表服务实现类
 * 表名: department
 *
 * @author G2rain Generator
 */
@Service(value = "departmentServiceImpl")
public class DepartmentServiceImpl implements DepartmentService {

    @Resource(name = "departmentDao")
    private DepartmentDao departmentDao;

    @Resource
    private DataPermissionPolicyCacheBroadcaster policyCacheBroadcaster;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DepartmentVo> selectList(DepartmentSelectDto selectDto) {
        return departmentDao.selectList(selectDto)
            .stream()
            .map(DepartmentConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<DepartmentVo> selectPage(PageSelectListDto<DepartmentSelectDto> selectDto) {
        Page<DepartmentPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            departmentDao.selectList(selectDto.getQuery());
        });
        List<DepartmentVo> result = page.getResult()
            .stream()
            .map(DepartmentConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public List<DepartmentTreeVo> selectTree(DepartmentSelectDto selectDto) {
        selectDto.setStatus(CommonStatus.ACTIVE.name());
        List<DepartmentPo> poList = departmentDao.selectList(selectDto);
        if (poList.isEmpty()) {
            return List.of();
        }

        Map<Long, DepartmentTreeVo> id2node = HashMap.newHashMap(poList.size());
        Map<Long, Integer> sortOrderById = HashMap.newHashMap(poList.size());
        for (DepartmentPo po : poList) {
            DepartmentTreeVo node = new DepartmentTreeVo();
            node.setDeptPath(po.getDeptPath());
            node.setDeptName(po.getDeptName());
            node.setChildren(new ArrayList<>());
            id2node.put(po.getId(), node);
            sortOrderById.put(po.getId(), po.getSortOrder());
        }

        List<DepartmentTreeVo> roots = new ArrayList<>();
        for (DepartmentPo po : poList) {
            DepartmentTreeVo node = id2node.get(po.getId());
            Long parentId = po.getParentId();
            if (Objects.isNull(parentId) || parentId.equals(DeptUtils.ROOT_PARENT_ID)) {
                roots.add(node);
                continue;
            }

            DepartmentTreeVo parent = id2node.get(parentId);
            if (Objects.isNull(parent)) {
                roots.add(node);
                continue;
            }

            parent.getChildren().add(node);
        }

        Map<DepartmentTreeVo, Long> node2id = new IdentityHashMap<>();
        for (Map.Entry<Long, DepartmentTreeVo> entry : id2node.entrySet()) {
            node2id.put(entry.getValue(), entry.getKey());
        }

        Comparator<DepartmentTreeVo> order = Comparator
            .comparing((DepartmentTreeVo node) -> sortOrderById.get(node2id.get(node)),
                Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(node2id::get, Comparator.nullsLast(Comparator.naturalOrder()));

        Queue<List<DepartmentTreeVo>> queue = new LinkedList<>();
        queue.add(roots);
        while (!queue.isEmpty()) {
            List<DepartmentTreeVo> level = queue.poll();
            level.sort(order);
            for (DepartmentTreeVo node : level) {
                if (!node.getChildren().isEmpty()) {
                    queue.add(node.getChildren());
                }
            }
        }

        roots.sort(order);
        return roots;
    }

    @Override
    public Long save(DepartmentDto dto) {
        // 转换 DTO 为 PO
        DepartmentPo entity = DepartmentConverter.INSTANCE.dto2po(dto);
        LocalDateTime now = Moments.now();
        entity.setUpdateTime(now);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.nonNull(id) && id > 0) {
            DepartmentPo before = departmentDao.selectById(id);
            Optional.ofNullable(dto.getParentId()).ifPresent(parentId -> {
                Asserts.isTrue(!parentId.equals(id), SystemErrorCode.PARAM_VAL_INVALID, "parentId");

                DepartmentPo department = departmentDao.selectById(id);
                Asserts.notNull(department, SystemErrorCode.RESOURCE_NOT_FOUND, "department", id);
                if (parentId.equals(department.getParentId())) {
                    return;
                }

                Long cursor = parentId;
                while (Objects.nonNull(cursor) && !cursor.equals(DeptUtils.ROOT_PARENT_ID)) {
                    if (cursor.equals(id)) {
                        throw new BusinessException(SystemErrorCode.PARAM_VAL_INVALID, "parentId");
                    }

                    department = departmentDao.selectById(cursor);
                    Asserts.notNull(department, SystemErrorCode.RESOURCE_NOT_FOUND, "department", cursor);
                    Asserts.isTrue(Objects.equals(department.getOrganId(), dto.getOrganId()),
                        SystemErrorCode.PARAM_VAL_INVALID, "parentId");
                    cursor = department.getParentId();
                }
            });

            // 更新：直接更新
            int success = departmentDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
            policyCacheBroadcaster.broadcastDepartmentLeaderChange(before, entity);
            return id;
        }

        Long parentId = Objects.requireNonNullElse(entity.getParentId(), DeptUtils.ROOT_PARENT_ID);
        entity.setParentId(parentId);

        String parentDeptPath = "";
        if (!Objects.equals(parentId, DeptUtils.ROOT_PARENT_ID)) {
            DepartmentPo parent = departmentDao.selectById(parentId);
            Asserts.notNull(parent, SystemErrorCode.RESOURCE_NOT_FOUND, "department", parentId);
            Asserts.isTrue(Objects.equals(parent.getOrganId(), entity.getOrganId()),
                SystemErrorCode.PARAM_VAL_INVALID, "parentId");
            parentDeptPath = parent.getDeptPath();
        }

        String maxSiblingDeptPath = departmentDao.selectMaxDeptPath(entity.getOrganId(), parentId);
        entity.setDeptPath(DeptUtils.nextDeptPath(parentDeptPath, maxSiblingDeptPath));
        entity.setStatus(CommonStatus.ACTIVE.name());

        // 新增：使用IdGenerator生成主键
        entity.setId(idGenerator.generateId());
        entity.setCreateTime(now);
        int success = departmentDao.insert(entity);
        Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        return departmentDao.delete(id);
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        DepartmentPo before = departmentDao.selectById(id);
        int updated = CommonStatusUpdater.update(
            id,
            dto,
            () -> {
                DepartmentPo entity = departmentDao.selectById(id);
                return entity == null ? null : entity.getStatus();
            },
            () -> {
                DepartmentPo entity = new DepartmentPo();
                entity.setId(id);
                entity.setStatus(dto.getStatus());
                entity.setUpdateTime(Moments.now());
                return departmentDao.update(entity);
            },
            "department"
        );
        if (updated > 0 && Objects.nonNull(before) && !Objects.equals(before.getStatus(), dto.getStatus())) {
            DepartmentPo after = departmentDao.selectById(id);
            policyCacheBroadcaster.broadcastDepartmentLeaderChange(before, after);
        }
        return updated;
    }
}
