package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DepartmentUserRelationConverter;
import com.g2rain.department.dao.DepartmentDao;
import com.g2rain.department.dao.DepartmentUserRelationDao;
import com.g2rain.department.dao.po.DepartmentPo;
import com.g2rain.department.dao.po.DepartmentUserRelationPo;
import com.g2rain.department.dto.DepartmentAssignUsersDto;
import com.g2rain.department.dto.DepartmentUserRelationDto;
import com.g2rain.department.dto.DepartmentUserRelationSelectDto;
import com.g2rain.department.service.DepartmentUserRelationService;
import com.g2rain.department.vo.DepartmentUserRelationVo;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门人员关系表服务实现类
 * 表名: department_user_relation
 *
 * @author G2rain Generator
 */
@Service(value = "departmentUserRelationServiceImpl")
public class DepartmentUserRelationServiceImpl implements DepartmentUserRelationService {

    @Resource(name = "departmentUserRelationDao")
    private DepartmentUserRelationDao departmentUserRelationDao;

    @Resource(name = "departmentDao")
    private DepartmentDao departmentDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DepartmentUserRelationVo> selectList(DepartmentUserRelationSelectDto selectDto) {
        return departmentUserRelationDao.selectList(selectDto)
            .stream()
            .map(DepartmentUserRelationConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<DepartmentUserRelationVo> selectPage(PageSelectListDto<DepartmentUserRelationSelectDto> selectDto) {
        Page<DepartmentUserRelationPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () ->
            departmentUserRelationDao.selectList(selectDto.getQuery())
        );
        List<DepartmentUserRelationVo> result = page.getResult()
            .stream()
            .map(DepartmentUserRelationConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public String getPrincipalEnrichment(Long organId, Long userId) {
        return String.join(",", departmentUserRelationDao.selectDeptPaths(organId, userId));
    }

    @Override
    public Long save(DepartmentUserRelationDto dto) {
        DepartmentUserRelationPo entity = DepartmentUserRelationConverter.INSTANCE.dto2po(dto);

        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = departmentUserRelationDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            entity.setUpdateTime(Moments.now());
            int success = departmentUserRelationDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    @Transactional
    public Integer addUsers(DepartmentAssignUsersDto dto) {
        Long organId = dto.getOrganId();
        Long departmentId = dto.getDepartmentId();
        Set<Long> userIds = dto.getUserIds();

        DepartmentPo department = departmentDao.selectById(departmentId);
        Asserts.isTrue(Objects.nonNull(department), SystemErrorCode.PARAM_VAL_INVALID, "departmentId");
        Asserts.isTrue(Objects.equals(department.getOrganId(), organId), SystemErrorCode.PARAM_VAL_INVALID, "organId");

        if (Collections.isEmpty(userIds)) {
            return 0;
        }

        DepartmentUserRelationSelectDto selectDto = new DepartmentUserRelationSelectDto();
        selectDto.setOrganId(organId);
        selectDto.setDepartmentId(departmentId);
        selectDto.setUserIds(userIds);
        Set<Long> associatedUserIds = departmentUserRelationDao.selectList(selectDto)
            .stream()
            .map(DepartmentUserRelationPo::getUserId)
            .collect(Collectors.toSet());

        userIds.removeIf(associatedUserIds::contains);
        if (Collections.isEmpty(userIds)) {
            return 0;
        }

        LocalDateTime now = Moments.now();
        List<DepartmentUserRelationPo> relations = userIds.stream().map(userId -> {
            DepartmentUserRelationPo relation = new DepartmentUserRelationPo();
            relation.setId(idGenerator.generateId());
            relation.setCreateTime(now);
            relation.setUpdateTime(now);
            relation.setOrganId(organId);
            relation.setDepartmentId(departmentId);
            relation.setUserId(userId);
            return relation;
        }).toList();

        return departmentUserRelationDao.insertMultiple(relations);
    }

    @Override
    public int delete(Long id) {
        return departmentUserRelationDao.delete(id);
    }
}
