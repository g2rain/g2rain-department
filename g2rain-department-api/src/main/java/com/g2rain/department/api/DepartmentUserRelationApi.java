package com.g2rain.department.api;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.dto.DepartmentUserRelationSelectDto;
import com.g2rain.department.vo.DepartmentUserRelationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 部门人员关系表API接口
 * 表名: department_user_relation
 *
 * @author G2rain Generator
 */
@Tag(name = "部门人员关系表", description = "部门人员关系表相关接口")
public interface DepartmentUserRelationApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询部门人员关系表列表", description = "根据查询条件返回部门人员关系表列表")
    Result<List<DepartmentUserRelationVo>> selectList(DepartmentUserRelationSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询部门人员关系表列表", description = "分页查询部门人员关系表列表")
    Result<PageData<DepartmentUserRelationVo>> selectPage(PageSelectListDto<DepartmentUserRelationSelectDto> selectDto);

    /**
     * 查询用户所在部门路径。
     *
     * @param organId 机构标识
     * @param userId  用户标识
     * @return 逗号拼接后的部门路径
     */
    @GetMapping("/principal_enrichment")
    @Operation(summary = "获取 Principal 增强信息", hidden = true, description = "根据机构标识和用户标识查询获取 Principal 增强信息")
    Result<String> getPrincipalEnrichment(@Parameter(description = "机构标识") @RequestParam Long organId, @Parameter(description = "用户标识") @RequestParam Long userId);
}
