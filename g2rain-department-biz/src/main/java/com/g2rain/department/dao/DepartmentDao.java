package com.g2rain.department.dao;

import com.g2rain.department.dao.po.DepartmentPo;
import com.g2rain.department.dto.DepartmentSelectDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门表数据访问接口
 * 表名: department
 *
 * @author G2rain Generator
 */
@Mapper
public interface DepartmentDao {

    /**
     * 插入单条记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(DepartmentPo entity);

    /**
     * 批量插入记录
     *
     * @param list 实体对象列表
     * @return 影响行数
     */
    int insertMultiple(List<DepartmentPo> list);

    /**
     * 根据 ID 更新记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int update(DepartmentPo entity);

    /**
     * 根据 ID 删除记录
     *
     * @param id 主键 ID
     * @return 影响行数
     */
    int delete(Long id);

    /**
     * 根据ID和Version更新记录（乐观锁更新）
     *
     * @param entity 实体对象（必须包含version字段）
     * @return 影响行数
     */
    int updateByVersion(DepartmentPo entity);

    /**
     * 根据 ID 查询记录
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    DepartmentPo selectById(Long id);

    /**
     * 根据查询入参 DTO 筛选列表
     *
     * @param selectDto 查询条件 DTO
     * @return 实体对象列表
     */
    List<DepartmentPo> selectList(DepartmentSelectDto selectDto);
}
