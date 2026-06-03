package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.department.converter.ContractConverter;
import com.g2rain.department.dao.ContractDao;
import com.g2rain.department.dao.po.ContractPo;
import com.g2rain.department.dto.ContractDto;
import com.g2rain.department.dto.ContractSelectDto;
import com.g2rain.department.service.ContractService;
import com.g2rain.department.vo.ContractVo;
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
 * 客户表服务实现类
 * 表名: contract
 *
 * @author G2rain Generator
 */
@Service(value = "contractServiceImpl")
public class ContractServiceImpl implements ContractService {

    @Resource(name = "contractDao")
    private ContractDao contractDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<ContractVo> selectList(ContractSelectDto selectDto) {
        return contractDao.selectList(selectDto)
                .stream()
                .map(ContractConverter.INSTANCE::po2vo)
                .toList();
    }

    @Override
    public PageData<ContractVo> selectPage(PageSelectListDto<ContractSelectDto> selectDto) {
        Page<ContractPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            contractDao.selectList(selectDto.getQuery());
        });
        List<ContractVo> result = page.getResult()
                .stream()
                .map(ContractConverter.INSTANCE::po2vo)
                .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(ContractDto dto) {
        // 转换DTO为PO
        ContractPo entity = ContractConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = contractDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = contractDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        return contractDao.delete(id);
    }
}