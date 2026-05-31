package com.g2rain.department.api;

import com.g2rain.department.dto.DataPermissionGroupSelectDto;
import com.g2rain.department.vo.DataPermissionGroupVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 数据权限小组表API接口
 * 表名: data_permission_group
 *
 * @author G2rain Generator
 */
@Tag(name = "数据权限小组表", description = "数据权限小组表相关接口")
public interface DataPermissionGroupApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询数据权限小组表列表", description = "根据查询条件返回数据权限小组表列表")
    Result<List<DataPermissionGroupVo>> selectList(DataPermissionGroupSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询数据权限小组表列表", description = "分页查询数据权限小组表列表")
    Result<PageData<DataPermissionGroupVo>> selectPage(PageSelectListDto<DataPermissionGroupSelectDto> selectDto);
}
