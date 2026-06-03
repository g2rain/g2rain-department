package com.g2rain.department.converter;

import com.g2rain.department.dao.po.DataPermissionPolicyPo;
import com.g2rain.department.vo.DataPermissionPolicyVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 数据权限策略转换器。
 */
@Mapper(uses = PermissionModeConverter.class)
public interface DataPermissionPolicyConverter {

    DataPermissionPolicyConverter INSTANCE = Mappers.getMapper(DataPermissionPolicyConverter.class);

    @Mapping(target = "otherPermRule", source = "permissionRule")
    @Mapping(target = "groupRead", source = "groupPermissionMode", qualifiedByName = "permissionModeIntToRead")
    @Mapping(target = "groupWrite", source = "groupPermissionMode", qualifiedByName = "permissionModeIntToWrite")
    @Mapping(target = "otherRead", source = "otherPermissionMode", qualifiedByName = "permissionModeIntToRead")
    @Mapping(target = "otherWrite", source = "otherPermissionMode", qualifiedByName = "permissionModeIntToWrite")
    DataPermissionPolicyVo po2vo(DataPermissionPolicyPo po);
}
