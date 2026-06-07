package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionOtherApi;
import com.g2rain.department.dto.DataPermissionOtherDto;
import com.g2rain.department.dto.DataPermissionOtherSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.service.DataPermissionOtherService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionOtherVo;
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
 * 数据权限Other规则表控制器
 * 表名: data_permission_other
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_other")
public class DataPermissionOtherController implements DataPermissionOtherApi {

    @Resource(name = "dataPermissionOtherServiceImpl")
    private DataPermissionOtherService dataPermissionOtherService;

    @Override
    public Result<List<DataPermissionOtherVo>> selectList(DataPermissionOtherSelectDto selectDto) {
        return Result.success(dataPermissionOtherService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionOtherVo>> selectPage(PageSelectListDto<DataPermissionOtherSelectDto> selectDto) {
        return Result.successPage(dataPermissionOtherService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限 Other 规则表信息", description = "新增或更新数据权限 Other 规则表基础信息")
    public Result<Long> save(@Validated @RequestBody DataPermissionOtherDto dto) {
        return Result.success(dataPermissionOtherService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限 Other 规则表记录", description = "根据主键删除数据权限 Other 规则表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限 Other 规则表标识") @PathVariable Long id) {
        return Result.success(dataPermissionOtherService.delete(id));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "修改规则配置状态", description = "修改规则配置状态")
    public Result<Integer> updateStatus(@Parameter(description = "数据权限 Other 规则表标识") @PathVariable Long id, @RequestBody @Validated UpdateStatusDto dto) {
        return Result.success(dataPermissionOtherService.updateStatus(id, dto));
    }
}
