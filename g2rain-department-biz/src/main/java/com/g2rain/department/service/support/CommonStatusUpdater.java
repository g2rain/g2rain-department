package com.g2rain.department.service.support;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.utils.Asserts;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * 通用状态更新辅助类
 */
public final class CommonStatusUpdater {

    private CommonStatusUpdater() {
    }

    public static int update(
        Long id,
        UpdateStatusDto dto,
        Supplier<String> currentStatusSupplier,
        IntSupplier updateSupplier,
        String resourceName
    ) {
        CommonStatus.validate(dto.getStatus());
        String currentStatus = currentStatusSupplier.get();
        Asserts.notNull(currentStatus, SystemErrorCode.RESOURCE_NOT_FOUND, resourceName, id);
        if (Objects.equals(currentStatus, dto.getStatus())) {
            return 1;
        }
        int updated = updateSupplier.getAsInt();
        Asserts.greaterThan(updated, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        return updated;
    }
}
