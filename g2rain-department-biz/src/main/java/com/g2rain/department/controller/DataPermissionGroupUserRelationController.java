package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionGroupUserRelationApi;
import com.g2rain.department.dto.DataPermissionGroupUserRelationDto;
import com.g2rain.department.dto.DataPermissionGroupUserRelationSelectDto;
import com.g2rain.department.service.DataPermissionGroupUserRelationService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionGroupUserRelationVo;
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
 * 数据权限小组人员关系表控制器
 * 表名: data_permission_group_user_relation
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_group_user_relation")
public class DataPermissionGroupUserRelationController implements DataPermissionGroupUserRelationApi {

    @Resource(name = "dataPermissionGroupUserRelationServiceImpl")
    private DataPermissionGroupUserRelationService dataPermissionGroupUserRelationService;

    @Override
    public Result<List<DataPermissionGroupUserRelationVo>> selectList(DataPermissionGroupUserRelationSelectDto selectDto) {
        return Result.success(dataPermissionGroupUserRelationService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionGroupUserRelationVo>> selectPage(PageSelectListDto<DataPermissionGroupUserRelationSelectDto> selectDto) {
        return Result.successPage(dataPermissionGroupUserRelationService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限小组人员关系表信息", description = "新增或更新数据权限小组人员关系表基础信息")
    public Result<Long> save(@RequestBody DataPermissionGroupUserRelationDto dto) {
        return Result.success(dataPermissionGroupUserRelationService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限小组人员关系表记录", description = "根据主键删除数据权限小组人员关系表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限小组人员关系表标识") @PathVariable Long id) {
        return Result.success(dataPermissionGroupUserRelationService.delete(id));
    }
}
