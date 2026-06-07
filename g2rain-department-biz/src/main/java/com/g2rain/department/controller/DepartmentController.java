package com.g2rain.department.controller;

import com.g2rain.department.api.DepartmentApi;
import com.g2rain.department.dto.DepartmentDto;
import com.g2rain.department.dto.DepartmentSelectDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.service.DepartmentService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DepartmentTreeVo;
import com.g2rain.department.vo.DepartmentVo;
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
 * 部门表控制器
 * 表名: department
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/department")
public class DepartmentController implements DepartmentApi {

    @Resource(name = "departmentServiceImpl")
    private DepartmentService departmentService;

    @Override
    public Result<List<DepartmentVo>> selectList(DepartmentSelectDto selectDto) {
        return Result.success(departmentService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DepartmentVo>> selectPage(PageSelectListDto<DepartmentSelectDto> selectDto) {
        return Result.successPage(departmentService.selectPage(selectDto));
    }

    @Override
    public Result<List<DepartmentTreeVo>> selectTree(DepartmentSelectDto selectDto) {
        return Result.success(departmentService.selectTree(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新部门表信息", description = "新增或更新部门表基础信息")
    public Result<Long> save(@Validated @RequestBody DepartmentDto dto) {
        return Result.success(departmentService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门表记录", description = "根据主键删除部门表记录")
    public Result<Integer> delete(@Parameter(description = "部门表标识") @PathVariable Long id) {
        return Result.success(departmentService.delete(id));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "修改部门状态", description = "修改部门状态")
    public Result<Integer> updateStatus(@Parameter(description = "部门表标识") @PathVariable Long id, @RequestBody @Validated UpdateStatusDto dto) {
        return Result.success(departmentService.updateStatus(id, dto));
    }
}
