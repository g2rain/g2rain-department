package com.g2rain.department.controller;

import com.g2rain.department.api.DepartmentUserRelationApi;
import com.g2rain.department.dto.DepartmentUserRelationDto;
import com.g2rain.department.dto.DepartmentUserRelationSelectDto;
import com.g2rain.department.service.DepartmentUserRelationService;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.vo.DepartmentUserRelationVo;
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
 * 部门人员关系表控制器
 * 表名: department_user_relation
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/department_user_relation")
public class DepartmentUserRelationController implements DepartmentUserRelationApi {

    @Resource(name = "departmentUserRelationServiceImpl")
    private DepartmentUserRelationService departmentUserRelationService;

    @Override
    public Result<List<DepartmentUserRelationVo>> selectList(DepartmentUserRelationSelectDto selectDto) {
        return Result.success(departmentUserRelationService.selectList(selectDto));
    }

    @Override
    public Result<PageData<DepartmentUserRelationVo>> selectPage(PageSelectListDto<DepartmentUserRelationSelectDto> selectDto) {
        return Result.successPage(departmentUserRelationService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新部门人员关系表信息", description = "新增或更新部门人员关系表基础信息")
    public Result<Long> save(@RequestBody DepartmentUserRelationDto dto) {
        return Result.success(departmentUserRelationService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门人员关系表记录", description = "根据主键删除部门人员关系表记录")
    public Result<Integer> delete(@Parameter(description = "部门人员关系表标识") @PathVariable Long id) {
        return Result.success(departmentUserRelationService.delete(id));
    }
}
