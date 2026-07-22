package com.g2rain.department.service;

import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.vo.DepartmentIdpSyncResultVo;

import java.util.List;

/**
 * 部门 IdP 同步服务。
 */
public interface DepartmentIdpSyncService {

    DepartmentIdpSyncResultVo sync(DepartmentIdpSyncDto dto);

    List<String> listMappedIdpDeptIds(Long organId, String idpType);
}
