package com.g2rain.department.converter;

import com.g2rain.common.converter.CommonConverter;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dto.DataPermissionMetaDto;
import com.g2rain.department.vo.DataPermissionMetaVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 数据权限元数据表转换器
 * 表名: data_permission_meta
 */
@Mapper(uses = {CommonConverter.class, PermissionModeConverter.class})
public interface DataPermissionMetaConverter {

    DataPermissionMetaConverter INSTANCE = Mappers.getMapper(DataPermissionMetaConverter.class);

    @Mapping(target = "read", source = "permissionMode", qualifiedByName = "permissionModeToRead")
    @Mapping(target = "write", source = "permissionMode", qualifiedByName = "permissionModeToWrite")
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    DataPermissionMetaVo po2vo(DataPermissionMetaPo po);

    @Mapping(target = "permissionMode", source = ".", qualifiedByName = "dtoToPermissionMode")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleteFlag", ignore = true)
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stringToLocalDateTime")
    DataPermissionMetaPo dto2po(DataPermissionMetaDto dto);
}
