package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限元数据表返回Po
 * 关联表名: data_permission_meta
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataPermissionMetaPo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 权限模型标识
     */
    private Long modelId;

    /**
     * 权限模式[例如 rw]
     */
    private Byte permissionMode;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
