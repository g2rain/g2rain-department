package com.g2rain.department.controller;

import com.g2rain.department.api.DataPermissionMetaApi;
import com.g2rain.department.dto.DataPermissionMetaDto;
import com.g2rain.department.dto.DataPermissionMetaSelectDto;
import com.g2rain.department.dto.DataPermissionPolicyResolveDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.service.DataPermissionMetaService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DataPermissionMetaVo;
import com.g2rain.department.vo.DataPermissionPolicyVo;
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
 * 数据权限元数据表控制器
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/data_permission_meta")
public class DataPermissionMetaController implements DataPermissionMetaApi {

    @Resource(name = "dataPermissionMetaServiceImpl")
    private DataPermissionMetaService dataPermissionMetaService;

    @Override
    public Result<List<DataPermissionMetaVo>> selectList(DataPermissionMetaSelectDto selectDto) {
        return Result.success(dataPermissionMetaService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DataPermissionMetaVo>> selectPage(PageSelectListDto<DataPermissionMetaSelectDto> selectDto) {
        return Result.successPage(dataPermissionMetaService.selectPage(selectDto));
    }

    @Override
    public Result<DataPermissionPolicyVo> resolveDataPermissionPolicy(DataPermissionPolicyResolveDto resolveDto) {
        return Result.success(dataPermissionMetaService.resolveDataPermissionPolicy(resolveDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数据权限元数据表信息", description = "新增或更新数据权限元数据表基础信息")
    public Result<Long> save(@Validated @RequestBody DataPermissionMetaDto dto) {
        return Result.success(dataPermissionMetaService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据权限元数据表记录", description = "根据主键删除数据权限元数据表记录")
    public Result<Integer> delete(@Parameter(description = "数据权限元数据表标识") @PathVariable Long id) {
        return Result.success(dataPermissionMetaService.delete(id));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "修改权限策略状态", description = "修改权限策略状态")
    public Result<Integer> updateStatus(@Parameter(description = "数据权限元数据表标识") @PathVariable Long id, @RequestBody @Validated UpdateStatusDto dto) {
        return Result.success(dataPermissionMetaService.updateStatus(id, dto));
    }
}
