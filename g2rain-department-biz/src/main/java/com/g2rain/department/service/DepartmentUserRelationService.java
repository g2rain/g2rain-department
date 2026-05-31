package com.g2rain.department.service;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.department.dto.DepartmentUserRelationDto;
import com.g2rain.department.dto.DepartmentUserRelationSelectDto;
import com.g2rain.department.vo.DepartmentUserRelationVo;

import java.util.List;

/**
 * 部门人员关系表服务接口
 * 表名: department_user_relation
 *
 * @author G2rain Generator
 */
public interface DepartmentUserRelationService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<DepartmentUserRelationVo> selectList(DepartmentUserRelationSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<DepartmentUserRelationVo> selectPage(PageSelectListDto<DepartmentUserRelationSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(DepartmentUserRelationDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);
}
