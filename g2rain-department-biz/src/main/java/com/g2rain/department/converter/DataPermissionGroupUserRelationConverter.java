package com.g2rain.department.converter;

import com.g2rain.department.dao.po.DataPermissionGroupUserRelationPo;
import com.g2rain.department.dto.DataPermissionGroupUserRelationDto;
import com.g2rain.common.converter.CommonConverter;
import com.g2rain.department.vo.DataPermissionGroupUserRelationVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * 数据权限小组人员关系表转换器
 * 用于Po、Vo、Dto之间的相互转换
 * 表名: data_permission_group_user_relation
 *
 * @author G2rain Generator
 */
@Mapper(uses = CommonConverter.class)
public interface DataPermissionGroupUserRelationConverter {

    /**
     * 单例实例，通过 {@link Mappers#getMapper(Class)} 获取 MapStruct 自动生成的实现。
     */
    DataPermissionGroupUserRelationConverter INSTANCE = Mappers.getMapper(DataPermissionGroupUserRelationConverter.class);

    /**
     * Po -> Vo
     * 自动将 createTime 和 updateTime 从 {@link LocalDateTime} 转换为 {@link String}
     */
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    DataPermissionGroupUserRelationVo po2vo(DataPermissionGroupUserRelationPo po);

    /**
     * Dto -> Po
     * 自动将 createTime 和 updateTime 从 {@link String} 转换为 {@link LocalDateTime}
     * 忽略 version 字段
     * 忽略 deleteFlag 字段
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleteFlag", ignore = true)
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stringToLocalDateTime")
    DataPermissionGroupUserRelationPo dto2po(DataPermissionGroupUserRelationDto dto);
}
