package com.g2rain.department.service.support;

import com.g2rain.department.dao.po.DataPermissionFieldPo;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dao.po.DataPermissionModelPo;
import com.g2rain.data.isolation.model.DataIsolationMeta;
import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 单表数据权限隔离解析上下文。
 */
@Getter
@Setter
public class DataPermissionIsolationContext {

    private Long organId;

    private Long userId;

    private String deptPathsCsv;

    private DataPermissionMetaPo metaPo;

    private DataPermissionModelPo modelPo;

    private List<DataPermissionFieldPo> fields;

    private DataIsolationMeta isolationMeta;

    private DataPermissionPolicyResolveResult policy;

    private boolean inGroup;
}
