package com.g2rain.department.controller;

import com.g2rain.common.model.Result;
import com.g2rain.department.api.DepartmentIdpSyncApi;
import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.service.DepartmentIdpSyncService;
import com.g2rain.department.vo.DepartmentIdpSyncResultVo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门 IdP 同步控制器。
 */
@RestController
@RequestMapping("/department_idp_sync")
public class DepartmentIdpSyncController implements DepartmentIdpSyncApi {

    @Resource(name = "departmentIdpSyncServiceImpl")
    private DepartmentIdpSyncService departmentIdpSyncService;

    @Override
    public Result<DepartmentIdpSyncResultVo> sync(@RequestBody @Validated DepartmentIdpSyncDto dto) {
        return Result.success(departmentIdpSyncService.sync(dto));
    }

    @Override
    public Result<List<String>> listMappedIdpDeptIds(
        @RequestParam("organId") Long organId,
        @RequestParam("idpType") String idpType
    ) {
        return Result.success(departmentIdpSyncService.listMappedIdpDeptIds(organId, idpType));
    }
}
