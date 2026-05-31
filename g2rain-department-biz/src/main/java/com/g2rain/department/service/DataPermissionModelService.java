package com.g2rain.department.service;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.department.dto.DataPermissionModelDto;
import com.g2rain.department.dto.DataPermissionModelSelectDto;
import com.g2rain.department.vo.DataPermissionModelVo;

import java.util.List;

/**
 * 数据权限模型全局元数据表服务接口
 * 表名: data_permission_model
 *
 * @author G2rain Generator
 */
public interface DataPermissionModelService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<DataPermissionModelVo> selectList(DataPermissionModelSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<DataPermissionModelVo> selectPage(PageSelectListDto<DataPermissionModelSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(DataPermissionModelDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);
}
