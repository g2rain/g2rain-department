package com.g2rain.department.api;

import com.g2rain.department.dto.DataPermissionMetaSelectDto;
import com.g2rain.department.vo.DataPermissionMetaVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.dto.DataPermissionPolicyResolveDto;
import com.g2rain.department.vo.DataPermissionPolicyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 数据权限元数据表API接口
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@Tag(name = "数据权限元数据表", description = "数据权限元数据表相关接口")
public interface DataPermissionMetaApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询数据权限元数据表列表", description = "根据查询条件返回数据权限元数据表列表")
    Result<List<DataPermissionMetaVo>> selectList(DataPermissionMetaSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询数据权限元数据表列表", description = "分页查询数据权限元数据表列表")
    Result<PageData<DataPermissionMetaVo>> selectPage(PageSelectListDto<DataPermissionMetaSelectDto> selectDto);

    /**
     * 解析数据权限策略。
     *
     * @param resolveDto 解析查询参数
     * @return 数据权限策略
     */
    @GetMapping("/policy_resolve")
    @Operation(summary = "解析数据权限策略", hidden = true, description = "根据用户、部门路径和业务模型解析数据权限策略")
    Result<DataPermissionPolicyVo> resolveDataPermissionPolicy(DataPermissionPolicyResolveDto resolveDto);
}
