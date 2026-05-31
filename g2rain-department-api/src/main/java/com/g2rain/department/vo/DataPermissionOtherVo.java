package com.g2rain.department.vo;

import com.g2rain.common.json.AdminCompanyCondition;
import com.g2rain.common.json.ConditionalJsonIgnore;
import com.g2rain.common.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据权限Other规则表返回VO
 * 关联表名: data_permission_other
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限Other规则表 VO")
public class DataPermissionOtherVo extends BaseVo {

    /**
     * 关联 data_permission_meta.id
     */
    @Schema(description = "关联 data_permission_meta.id")
    private Long metaId;

    /**
     * 分组标识
     */
    @Schema(description = "分组标识")
    private Long groupId;

    /**
     * 权限模式[例如 rw]
     */
    @Schema(description = "权限模式[例如 rw]")
    private Byte permissionMode;

    /**
     * 状态[ACTIVE:有效, INACTIVE:停用]
     */
    @Schema(description = "状态[ACTIVE:有效, INACTIVE:停用]")
    private String status;

    /**
     * 权限规则
     */
    @Schema(description = "权限规则")
    private String permissionRule;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}
