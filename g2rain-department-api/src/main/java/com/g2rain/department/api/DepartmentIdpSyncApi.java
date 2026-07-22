package com.g2rain.department.api;

import com.g2rain.common.model.Result;
import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.vo.DepartmentIdpSyncResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 部门 IdP 同步 API。
 */
@Tag(name = "部门 IdP 同步", description = "按 IdP 部门树初始化部门并关联成员")
public interface DepartmentIdpSyncApi {

    /**
     * 同步 IdP 部门树与成员关系。
     */
    @PostMapping("/sync")
    @Operation(summary = "同步 IdP 部门与成员关系", description = "按 IdP 部门树 upsert 平台部门并批量关联成员")
    Result<DepartmentIdpSyncResultVo> sync(@RequestBody @Validated DepartmentIdpSyncDto dto);

    /**
     * 查询机构下已映射的 IdP 部门标识（供同步安全闸使用）。
     */
    @GetMapping("/mapped_idp_dept_ids")
    @Operation(summary = "查询已映射 IdP 部门 ID", description = "供租户 IdP 同步安全闸评估使用")
    Result<List<String>> listMappedIdpDeptIds(
        @RequestParam("organId") Long organId,
        @RequestParam("idpType") String idpType
    );
}
