package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DataPermissionMetaConverter;
import com.g2rain.department.dao.DataPermissionMetaDao;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dto.DataPermissionMetaDto;
import com.g2rain.department.dto.DataPermissionMetaSelectDto;
import com.g2rain.department.service.DataPermissionMetaService;
import com.g2rain.department.vo.DataPermissionMetaVo;
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
 * 数据权限元数据表服务实现类
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionMetaServiceImpl")
public class DataPermissionMetaServiceImpl implements DataPermissionMetaService {

    @Resource(name = "dataPermissionMetaDao")
    private DataPermissionMetaDao dataPermissionMetaDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionMetaVo> selectList(DataPermissionMetaSelectDto selectDto) {
        return dataPermissionMetaDao.selectList(selectDto)
                .stream()
                .map(DataPermissionMetaConverter.INSTANCE::po2vo)
                .toList();
    }

    @Override
    public PageData<DataPermissionMetaVo> selectPage(PageSelectListDto<DataPermissionMetaSelectDto> selectDto) {
        Page<DataPermissionMetaPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            dataPermissionMetaDao.selectList(selectDto.getQuery());
        });
        List<DataPermissionMetaVo> result = page.getResult()
                .stream()
                .map(DataPermissionMetaConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DataPermissionMetaDto dto) {
        // 转换 DTO 为 PO
        DataPermissionMetaPo entity = DataPermissionMetaConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = dataPermissionMetaDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionMetaDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        return dataPermissionMetaDao.delete(id);
    }
}
