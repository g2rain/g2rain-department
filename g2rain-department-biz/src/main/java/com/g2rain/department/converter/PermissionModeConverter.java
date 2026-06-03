package com.g2rain.department.converter;

import com.g2rain.department.dto.DataPermissionMetaDto;
import com.g2rain.department.dto.DataPermissionOtherDto;
import org.mapstruct.Named;

/**
 * 权限模式位转换：permissionMode 与 read/write 互转。
 * <p>
 * 低位 bit0 = 写，bit1 = 读：--=0, -w=1, r-=2, rw=3
 * </p>
 */
public class PermissionModeConverter {

    private static final int READ_MASK = 0b10;
    private static final int WRITE_MASK = 0b01;

    @Named("permissionModeToRead")
    public boolean permissionModeToRead(Byte permissionMode) {
        return canRead(permissionMode == null ? null : permissionMode.intValue());
    }

    @Named("permissionModeToWrite")
    public boolean permissionModeToWrite(Byte permissionMode) {
        return canWrite(permissionMode == null ? null : permissionMode.intValue());
    }

    @Named("permissionModeIntToRead")
    public boolean permissionModeIntToRead(Integer permissionMode) {
        return canRead(permissionMode);
    }

    @Named("permissionModeIntToWrite")
    public boolean permissionModeIntToWrite(Integer permissionMode) {
        return canWrite(permissionMode);
    }

    private boolean canRead(Integer permissionMode) {
        return permissionMode != null && (permissionMode & READ_MASK) != 0;
    }

    private boolean canWrite(Integer permissionMode) {
        return permissionMode != null && (permissionMode & WRITE_MASK) != 0;
    }

    @Named("readWriteToPermissionMode")
    public Byte readWriteToPermissionMode(boolean read, boolean write) {
        int mode = 0;
        if (write) {
            mode |= WRITE_MASK;
        }
        if (read) {
            mode |= READ_MASK;
        }
        return (byte) mode;
    }

    @Named("dtoToPermissionMode")
    public Byte dtoToPermissionMode(DataPermissionMetaDto dto) {
        return readWriteToPermissionMode(dto.isRead(), dto.isWrite());
    }

    @Named("otherDtoToPermissionMode")
    public Byte otherDtoToPermissionMode(DataPermissionOtherDto dto) {
        return readWriteToPermissionMode(dto.isRead(), dto.isWrite());
    }
}
