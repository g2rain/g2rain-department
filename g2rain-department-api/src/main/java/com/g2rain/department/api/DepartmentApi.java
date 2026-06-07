package com.g2rain.department.api;

import com.g2rain.department.dto.DepartmentSelectDto;
import com.g2rain.department.vo.DepartmentTreeVo;
import com.g2rain.department.vo.DepartmentVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 部门表API接口
 * 表名: department
 *
 * @author G2rain Generator
 */
@Tag(name = "部门表", description = "部门表相关接口")
public interface DepartmentApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询部门表列表", description = "根据查询条件返回部门表列表")
    Result<List<DepartmentVo>> selectList(DepartmentSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询部门表列表", description = "分页查询部门表列表")
    Result<PageData<DepartmentVo>> selectPage(PageSelectListDto<DepartmentSelectDto> selectDto);

    /**
     * 查询部门树形结构
     *
     * @param selectDto 查询条件 DTO
     * @return 部门树形结构
     */
    @GetMapping("/tree")
    @Operation(summary = "查询部门树形结构", description = "根据查询条件返回有层级关系的部门树")
    Result<List<DepartmentTreeVo>> selectTree(DepartmentSelectDto selectDto);
}
