package com.g2rain.department.service.support;

import com.g2rain.data.isolation.model.DataPermissionPolicyResolveResult;
import com.g2rain.department.vo.DataPermissionPolicyVo;

import java.util.Objects;

/**
 * 将 department 策略 VO 转为 starter 策略结果。
 */
public final class DataPermissionPolicyResolveResultConverter {

    private DataPermissionPolicyResolveResultConverter() {
    }

    public static DataPermissionPolicyResolveResult fromVo(DataPermissionPolicyVo vo) {
        if (Objects.isNull(vo)) {
            return null;
        }
        DataPermissionPolicyResolveResult result = new DataPermissionPolicyResolveResult();
        result.setMetaId(vo.getMetaId());
        result.setGroupRead(vo.isGroupRead());
        result.setGroupWrite(vo.isGroupWrite());
        result.setOtherRead(vo.isOtherRead());
        result.setOtherWrite(vo.isOtherWrite());
        result.setOtherPermRule(vo.getOtherPermRule());
        return result;
    }
}
