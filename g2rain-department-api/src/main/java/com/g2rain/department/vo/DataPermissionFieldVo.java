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
 * 数据权限模型字段明细表返回VO
 * 关联表名: data_permission_field
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限模型字段明细表 VO")
public class DataPermissionFieldVo extends BaseVo {

    /**
     * 权限模型标识
     */
    @Schema(description = "权限模型标识")
    private Long modelId;

    /**
     * 业务表中的物理字段名，如 dept_path, owner_user_id
     */
    @Schema(description = "业务表中的物理字段名，如 dept_path, owner_user_id")
    private String fieldName;

    /**
     * 前端显示的中文标签，如 所属部门, 负责人
     */
    @Schema(description = "前端显示的中文标签，如 所属部门, 负责人")
    private String fieldTitle;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    /**
     * 删除标识[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}
