package com.g2rain.department.service;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.department.dto.DataPermissionGroupUserRelationDto;
import com.g2rain.department.dto.DataPermissionGroupUserRelationSelectDto;
import com.g2rain.department.vo.DataPermissionGroupUserRelationVo;

import java.util.List;

/**
 * 数据权限小组人员关系表服务接口
 * 表名: data_permission_group_user_relation
 *
 * @author G2rain Generator
 */
public interface DataPermissionGroupUserRelationService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<DataPermissionGroupUserRelationVo> selectList(DataPermissionGroupUserRelationSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<DataPermissionGroupUserRelationVo> selectPage(PageSelectListDto<DataPermissionGroupUserRelationSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(DataPermissionGroupUserRelationDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);
}
