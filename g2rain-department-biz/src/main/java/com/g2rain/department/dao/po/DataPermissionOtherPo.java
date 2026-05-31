package com.g2rain.department.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数据权限Other规则表返回Po
 * 关联表名: data_permission_other
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataPermissionOtherPo extends BasePo {

    /**
     * 关联 data_permission_meta.id
     */
    private Long metaId;

    /**
     * 分组标识
     */
    private Long groupId;

    /**
     * 权限模式[例如 rw]
     */
    private Byte permissionMode;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    private String status;

    /**
     * 权限规则
     */
    private String permissionRule;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}
