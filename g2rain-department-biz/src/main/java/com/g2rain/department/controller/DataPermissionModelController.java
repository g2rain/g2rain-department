package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionModelApi;
import com.g2rain.department.dto.DataPermissionModelDto;
import com.g2rain.department.dto.DataPermissionModelSelectDto;
import com.g2rain.department.service.DataPermissionModelService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionModelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据权限模型全局元数据表控制器
 * 表名: data_permission_model
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_model")
public class DataPermissionModelController implements DataPermissionModelApi {

    @Resource(name = "dataPermissionModelServiceImpl")
    private DataPermissionModelService dataPermissionModelService;

    @Override
    public Result<List<DataPermissionModelVo>> selectList(DataPermissionModelSelectDto selectDto) {
        return Result.success(dataPermissionModelService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionModelVo>> selectPage(PageSelectListDto<DataPermissionModelSelectDto> selectDto) {
        return Result.successPage(dataPermissionModelService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限模型全局元数据表信息", description = "新增或更新数据权限模型全局元数据表基础信息")
    public Result<Long> save(@Validated @RequestBody DataPermissionModelDto dto) {
        return Result.success(dataPermissionModelService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限模型全局元数据表记录", description = "根据主键删除数据权限模型全局元数据表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限模型全局元数据表标识") @PathVariable Long id) {
        return Result.success(dataPermissionModelService.delete(id));
    }
}
