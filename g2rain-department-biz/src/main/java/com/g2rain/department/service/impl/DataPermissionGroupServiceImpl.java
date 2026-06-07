package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DataPermissionGroupConverter;
import com.g2rain.department.dao.DataPermissionGroupDao;
import com.g2rain.department.dao.po.DataPermissionGroupPo;
import com.g2rain.department.dto.DataPermissionGroupDto;
import com.g2rain.department.dto.DataPermissionGroupSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.service.DataPermissionGroupService;
import com.g2rain.department.service.support.CommonStatusUpdater;
import com.g2rain.department.service.support.DataPermissionPolicyCacheBroadcaster;
import com.g2rain.department.service.support.DataPermissionPolicyChangeDetector;
import com.g2rain.department.vo.DataPermissionGroupVo;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 数据权限小组表服务实现类
 * 表名: data_permission_group
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionGroupServiceImpl")
public class DataPermissionGroupServiceImpl implements DataPermissionGroupService {

    @Resource(name = "dataPermissionGroupDao")
    private DataPermissionGroupDao dataPermissionGroupDao;

    @Resource
    private DataPermissionPolicyCacheBroadcaster policyCacheBroadcaster;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionGroupVo> selectList(DataPermissionGroupSelectDto selectDto) {
        return dataPermissionGroupDao.selectList(selectDto)
                .stream()
                .map(DataPermissionGroupConverter.INSTANCE::po2vo)
                .toList();
    }

    @Override
    public PageData<DataPermissionGroupVo> selectPage(PageSelectListDto<DataPermissionGroupSelectDto> selectDto) {
        Page<DataPermissionGroupPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            dataPermissionGroupDao.selectList(selectDto.getQuery());
        });
        List<DataPermissionGroupVo> result = page.getResult()
                .stream()
                .map(DataPermissionGroupConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DataPermissionGroupDto dto) {
        DataPermissionGroupPo entity = DataPermissionGroupConverter.INSTANCE.dto2po(dto);

        Long id = entity.getId();
        DataPermissionGroupPo before = Objects.nonNull(id) && id > 0 ? dataPermissionGroupDao.selectById(id) : null;
        if (Objects.isNull(id) || id == 0) {
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            entity.setStatus(CommonStatus.ACTIVE.name());
            int success = dataPermissionGroupDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionGroupDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
            if (DataPermissionPolicyChangeDetector.groupAffecting(before, entity)) {
                policyCacheBroadcaster.broadcastByGroupId(entity.getOrganId(), entity.getId());
            }
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        DataPermissionGroupPo group = dataPermissionGroupDao.selectById(id);
        int deleted = dataPermissionGroupDao.delete(id);
        if (deleted > 0 && Objects.nonNull(group)) {
            policyCacheBroadcaster.broadcastByGroupId(group.getOrganId(), group.getId());
        }
        return deleted;
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        DataPermissionGroupPo before = dataPermissionGroupDao.selectById(id);
        int updated = CommonStatusUpdater.update(
            id,
            dto,
            () -> {
                DataPermissionGroupPo entity = dataPermissionGroupDao.selectById(id);
                return entity == null ? null : entity.getStatus();
            },
            () -> {
                DataPermissionGroupPo entity = new DataPermissionGroupPo();
                entity.setId(id);
                entity.setStatus(dto.getStatus());
                entity.setUpdateTime(Moments.now());
                return dataPermissionGroupDao.update(entity);
            },
            "dataPermissionGroup"
        );
        if (updated > 0 && Objects.nonNull(before) && !Objects.equals(before.getStatus(), dto.getStatus())) {
            policyCacheBroadcaster.broadcastByGroupId(before.getOrganId(), id);
        }
        return updated;
    }
}
