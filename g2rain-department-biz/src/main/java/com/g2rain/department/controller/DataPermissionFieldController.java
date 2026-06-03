package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionFieldApi;
import com.g2rain.department.dto.DataPermissionFieldDto;
import com.g2rain.department.dto.DataPermissionFieldSelectDto;
import com.g2rain.department.service.DataPermissionFieldService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionFieldVo;
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
 * 数据权限模型字段明细表控制器
 * 表名: data_permission_field
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_field")
public class DataPermissionFieldController implements DataPermissionFieldApi {

    @Resource(name = "dataPermissionFieldServiceImpl")
    private DataPermissionFieldService dataPermissionFieldService;

    @Override
    public Result<List<DataPermissionFieldVo>> selectList(DataPermissionFieldSelectDto selectDto) {
        return Result.success(dataPermissionFieldService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionFieldVo>> selectPage(PageSelectListDto<DataPermissionFieldSelectDto> selectDto) {
        return Result.successPage(dataPermissionFieldService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限模型字段明细表信息", description = "新增或更新数据权限模型字段明细表基础信息")
    public Result<Long> save(@Validated @RequestBody DataPermissionFieldDto dto) {
        return Result.success(dataPermissionFieldService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限模型字段明细表记录", description = "根据主键删除数据权限模型字段明细表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限模型字段明细表标识") @PathVariable Long id) {
        return Result.success(dataPermissionFieldService.delete(id));
    }
}
