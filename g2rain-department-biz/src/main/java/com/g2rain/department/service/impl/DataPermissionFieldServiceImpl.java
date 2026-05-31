package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DataPermissionFieldConverter;
import com.g2rain.department.dao.DataPermissionFieldDao;
import com.g2rain.department.dao.po.DataPermissionFieldPo;
import com.g2rain.department.dto.DataPermissionFieldDto;
import com.g2rain.department.dto.DataPermissionFieldSelectDto;
import com.g2rain.department.service.DataPermissionFieldService;
import com.g2rain.department.vo.DataPermissionFieldVo;
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
 * 数据权限模型字段明细表服务实现类
 * 表名: data_permission_field
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionFieldServiceImpl")
public class DataPermissionFieldServiceImpl implements DataPermissionFieldService {

    @Resource(name = "dataPermissionFieldDao")
    private DataPermissionFieldDao dataPermissionFieldDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionFieldVo> selectList(DataPermissionFieldSelectDto selectDto) {
        return dataPermissionFieldDao.selectList(selectDto)
                .stream()
                .map(DataPermissionFieldConverter.INSTANCE::po2vo)
                .toList();
    }

    @Override
    public PageData<DataPermissionFieldVo> selectPage(PageSelectListDto<DataPermissionFieldSelectDto> selectDto) {
        Page<DataPermissionFieldPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            dataPermissionFieldDao.selectList(selectDto.getQuery());
        });
        List<DataPermissionFieldVo> result = page.getResult()
                .stream()
                .map(DataPermissionFieldConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DataPermissionFieldDto dto) {
        // 转换 DTO 为 PO
        DataPermissionFieldPo entity = DataPermissionFieldConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = dataPermissionFieldDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionFieldDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        return dataPermissionFieldDao.delete(id);
    }
}
