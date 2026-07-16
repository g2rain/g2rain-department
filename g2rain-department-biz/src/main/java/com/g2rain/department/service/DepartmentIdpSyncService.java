package com.g2rain.department.service;

import com.g2rain.department.dto.DepartmentIdpSyncDto;
import com.g2rain.department.vo.DepartmentIdpSyncResultVo;

/**
 * 部门 IdP 同步服务。
 */
public interface DepartmentIdpSyncService {

    DepartmentIdpSyncResultVo sync(DepartmentIdpSyncDto dto);
}
