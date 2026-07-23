package com.g2rain.department.enums;

import com.g2rain.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 部门模块错误码
 */
@Schema(description = "部门模块错误码枚举")
public enum DepartmentErrorCode implements ErrorCode {

    @Schema(description = "权限模型不存在")
    DATA_PERMISSION_MODEL_NOT_FOUND("department.40001", "权限模型不存在"),

    @Schema(description = "同一机构下权限模型已存在")
    DATA_PERMISSION_META_MODEL_DUPLICATE("department.40002", "同一机构下该权限模型已存在"),

    @Schema(description = "同一权限小组下权限策略已存在")
    DATA_PERMISSION_OTHER_META_DUPLICATE("department.40003", "同一权限小组下该权限策略已存在"),

    @Schema(description = "无权执行部门 IdP 同步")
    DEPARTMENT_IDP_SYNC_FORBIDDEN("department.40004", "无权执行部门 IdP 同步"),

    @Schema(description = "缺少登录用户或机构上下文")
    DATA_PERMISSION_IDENTITY_REQUIRED("department.40005", "缺少登录用户或机构上下文");

    private final String code;
    private final String messageTemplate;

    DepartmentErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String messageTemplate() {
        return messageTemplate;
    }
}
