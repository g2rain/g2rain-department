package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionGroupApi;
import com.g2rain.department.dto.DataPermissionGroupDto;
import com.g2rain.department.dto.DataPermissionGroupSelectDto;
import com.g2rain.department.service.DataPermissionGroupService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionGroupVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据权限小组表控制器
 * 表名: data_permission_group
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_group")
public class DataPermissionGroupController implements DataPermissionGroupApi {

    @Resource(name = "dataPermissionGroupServiceImpl")
    private DataPermissionGroupService dataPermissionGroupService;

    @Override
    public Result<List<DataPermissionGroupVo>> selectList(DataPermissionGroupSelectDto selectDto) {
        return Result.success(dataPermissionGroupService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionGroupVo>> selectPage(PageSelectListDto<DataPermissionGroupSelectDto> selectDto) {
        return Result.successPage(dataPermissionGroupService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限小组表信息", description = "新增或更新数据权限小组表基础信息")
    public Result<Long> save(@RequestBody DataPermissionGroupDto dto) {
        return Result.success(dataPermissionGroupService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限小组表记录", description = "根据主键删除数据权限小组表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限小组表标识") @PathVariable Long id) {
        return Result.success(dataPermissionGroupService.delete(id));
    }
}
