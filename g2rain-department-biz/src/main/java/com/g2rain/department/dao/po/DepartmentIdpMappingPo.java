package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * IdP 部门映射 PO。
 * 表名: department_idp_mapping
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentIdpMappingPo extends BasePo {

    private Long organId;

    private String idpType;

    private String idpDeptId;

    private Long departmentId;

    private Boolean deleteFlag;
}
