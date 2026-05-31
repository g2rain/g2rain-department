package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.DepartmentUserRelationConverter;
import com.g2rain.department.dao.DepartmentUserRelationDao;
import com.g2rain.department.dao.po.DepartmentUserRelationPo;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        Page<DepartmentUserRelationPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            departmentUserRelationDao.selectList(selectDto.getQuery());
        });
        List<DepartmentUserRelationVo> result = page.getResult()
                .stream()
                .map(DepartmentUserRelationConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(DepartmentUserRelationDto dto) {
        // 转换 DTO 为 PO
        DepartmentUserRelationPo entity = DepartmentUserRelationConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = departmentUserRelationDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = departmentUserRelationDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        return departmentUserRelationDao.delete(id);
    }
}
