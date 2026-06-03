package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DataPermissionGroupUserRelationConverter;
import com.g2rain.department.dao.DataPermissionGroupDao;
import com.g2rain.department.dao.DataPermissionGroupUserRelationDao;
import com.g2rain.department.dao.po.DataPermissionGroupPo;
import com.g2rain.department.dao.po.DataPermissionGroupUserRelationPo;
import com.g2rain.department.dto.DataPermissionGroupUserRelationDto;
import com.g2rain.department.dto.DataPermissionGroupUserRelationSelectDto;
import com.g2rain.department.dto.GroupAssignUsersDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.service.DataPermissionGroupUserRelationService;
import com.g2rain.department.service.support.CommonStatusUpdater;
import com.g2rain.department.vo.DataPermissionGroupUserRelationVo;
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
 * 数据权限小组人员关系表服务实现类
 * 表名: data_permission_group_user_relation
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionGroupUserRelationServiceImpl")
public class DataPermissionGroupUserRelationServiceImpl implements DataPermissionGroupUserRelationService {

    @Resource(name = "dataPermissionGroupUserRelationDao")
    private DataPermissionGroupUserRelationDao dataPermissionGroupUserRelationDao;

    @Resource(name = "dataPermissionGroupDao")
    private DataPermissionGroupDao dataPermissionGroupDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionGroupUserRelationVo> selectList(DataPermissionGroupUserRelationSelectDto selectDto) {
        return dataPermissionGroupUserRelationDao.selectList(selectDto)
            .stream()
            .map(DataPermissionGroupUserRelationConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<DataPermissionGroupUserRelationVo> selectPage(PageSelectListDto<DataPermissionGroupUserRelationSelectDto> selectDto) {
        Page<DataPermissionGroupUserRelationPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () ->
            dataPermissionGroupUserRelationDao.selectList(selectDto.getQuery())
        );
        List<DataPermissionGroupUserRelationVo> result = page.getResult()
            .stream()
            .map(DataPermissionGroupUserRelationConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DataPermissionGroupUserRelationDto dto) {
        // 转换 DTO 为 PO
        DataPermissionGroupUserRelationPo entity = DataPermissionGroupUserRelationConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = dataPermissionGroupUserRelationDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionGroupUserRelationDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    @Transactional
    public Integer addUsers(GroupAssignUsersDto dto) {
        Set<Long> userIds = dto.getUserIds();
        if (Collections.isEmpty(userIds)) {
            return 0;
        }

        Long organId = dto.getOrganId();
        Long groupId = dto.getGroupId();
        DataPermissionGroupPo group = dataPermissionGroupDao.selectById(groupId);
        Asserts.isTrue(Objects.nonNull(group), SystemErrorCode.PARAM_VAL_INVALID, "groupId");
        Asserts.isTrue(Objects.equals(group.getOrganId(), organId), SystemErrorCode.PARAM_VAL_INVALID, "organId");

        DataPermissionGroupUserRelationSelectDto selectDto = new DataPermissionGroupUserRelationSelectDto();
        selectDto.setOrganId(organId);
        selectDto.setGroupId(groupId);
        Set<Long> associatedUserIds = dataPermissionGroupUserRelationDao.selectList(selectDto)
            .stream()
            .map(DataPermissionGroupUserRelationPo::getUserId)
            .collect(Collectors.toSet());

        userIds.removeIf(associatedUserIds::contains);
        if (Collections.isEmpty(userIds)) {
            return 0;
        }

        LocalDateTime now = Moments.now();
        List<DataPermissionGroupUserRelationPo> relations = userIds.stream().map(userId -> {
            DataPermissionGroupUserRelationPo relation = new DataPermissionGroupUserRelationPo();
            relation.setId(idGenerator.generateId());
            relation.setCreateTime(now);
            relation.setUpdateTime(now);
            relation.setOrganId(organId);
            relation.setGroupId(groupId);
            relation.setUserId(userId);
            relation.setStatus(CommonStatus.ACTIVE.name());
            return relation;
        }).toList();

        return dataPermissionGroupUserRelationDao.insertMultiple(relations);
    }

    @Override
    public int delete(Long id) {
        return dataPermissionGroupUserRelationDao.delete(id);
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        return CommonStatusUpdater.update(
            id,
            dto,
            () -> {
                DataPermissionGroupUserRelationPo entity = dataPermissionGroupUserRelationDao.selectById(id);
                return entity == null ? null : entity.getStatus();
            },
            () -> {
                DataPermissionGroupUserRelationPo entity = new DataPermissionGroupUserRelationPo();
                entity.setId(id);
                entity.setStatus(dto.getStatus());
                entity.setUpdateTime(Moments.now());
                return dataPermissionGroupUserRelationDao.update(entity);
            },
            "dataPermissionGroupUserRelation"
        );
    }
}
