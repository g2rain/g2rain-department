package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DataPermissionOtherConverter;
import com.g2rain.department.dao.DataPermissionOtherDao;
import com.g2rain.department.dao.po.DataPermissionOtherPo;
import com.g2rain.department.dto.DataPermissionOtherDto;
import com.g2rain.department.dto.DataPermissionOtherSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.enums.DepartmentErrorCode;
import com.g2rain.department.service.DataPermissionOtherService;
import com.g2rain.department.service.support.CommonStatusUpdater;
import com.g2rain.department.service.support.DataPermissionPolicyCacheBroadcaster;
import com.g2rain.department.service.support.DataPermissionPolicyChangeDetector;
import com.g2rain.department.vo.DataPermissionOtherVo;
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
 * 数据权限Other规则表服务实现类
 * 表名: data_permission_other
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionOtherServiceImpl")
public class DataPermissionOtherServiceImpl implements DataPermissionOtherService {

    @Resource(name = "dataPermissionOtherDao")
    private DataPermissionOtherDao dataPermissionOtherDao;

    @Resource
    private DataPermissionPolicyCacheBroadcaster policyCacheBroadcaster;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionOtherVo> selectList(DataPermissionOtherSelectDto selectDto) {
        return dataPermissionOtherDao.selectList(selectDto)
                .stream()
                .map(DataPermissionOtherConverter.INSTANCE::po2vo)
                .toList();
    }

    @Override
    public PageData<DataPermissionOtherVo> selectPage(PageSelectListDto<DataPermissionOtherSelectDto> selectDto) {
        Page<DataPermissionOtherPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            dataPermissionOtherDao.selectList(selectDto.getQuery());
        });
        List<DataPermissionOtherVo> result = page.getResult()
                .stream()
                .map(DataPermissionOtherConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DataPermissionOtherDto dto) {
        DataPermissionOtherSelectDto selectDto = new DataPermissionOtherSelectDto();
        selectDto.setGroupId(dto.getGroupId());
        selectDto.setMetaId(dto.getMetaId());
        Long currentId = dto.getId();
        boolean duplicated = dataPermissionOtherDao.selectList(selectDto).stream()
            .anyMatch(item -> !Objects.equals(item.getId(), currentId));
        Asserts.isTrue(!duplicated, DepartmentErrorCode.DATA_PERMISSION_OTHER_META_DUPLICATE);

        DataPermissionOtherPo entity = DataPermissionOtherConverter.INSTANCE.dto2po(dto);

        Long id = entity.getId();
        DataPermissionOtherPo before = Objects.nonNull(id) && id > 0 ? dataPermissionOtherDao.selectById(id) : null;
        if (Objects.isNull(id) || id == 0) {
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            entity.setStatus(CommonStatus.ACTIVE.name());
            int success = dataPermissionOtherDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
            policyCacheBroadcaster.broadcastByOther(entity);
        } else {
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionOtherDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
            if (DataPermissionPolicyChangeDetector.otherAffecting(before, entity)) {
                policyCacheBroadcaster.broadcastOtherChange(before, entity);
            }
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        DataPermissionOtherPo other = dataPermissionOtherDao.selectById(id);
        int deleted = dataPermissionOtherDao.delete(id);
        if (deleted > 0) {
            policyCacheBroadcaster.broadcastByOther(other);
        }
        return deleted;
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        DataPermissionOtherPo before = dataPermissionOtherDao.selectById(id);
        int updated = CommonStatusUpdater.update(
            id,
            dto,
            () -> {
                DataPermissionOtherPo entity = dataPermissionOtherDao.selectById(id);
                return entity == null ? null : entity.getStatus();
            },
            () -> {
                DataPermissionOtherPo entity = new DataPermissionOtherPo();
                entity.setId(id);
                entity.setStatus(dto.getStatus());
                entity.setUpdateTime(Moments.now());
                return dataPermissionOtherDao.update(entity);
            },
            "dataPermissionOther"
        );
        if (updated > 0 && Objects.nonNull(before) && !Objects.equals(before.getStatus(), dto.getStatus())) {
            policyCacheBroadcaster.broadcastByOther(before);
        }
        return updated;
    }
}
