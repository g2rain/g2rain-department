package com.g2rain.department.dao;

import com.g2rain.data.isolation.annotations.DataIsolation;
import com.g2rain.data.isolation.annotations.IgnoreIsolation;
import com.g2rain.department.dao.po.DepartmentIdpMappingPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IdP 部门映射 DAO。
 */
@Mapper
@DataIsolation(organIdPropertyName = "organId", organIdColumnName = "organ_id")
public interface DepartmentIdpMappingDao {

    @IgnoreIsolation
    int insert(DepartmentIdpMappingPo entity);

    @IgnoreIsolation
    DepartmentIdpMappingPo selectByOrganAndIdpDept(
        @Param("organId") Long organId,
        @Param("idpType") String idpType,
        @Param("idpDeptId") String idpDeptId
    );

    @IgnoreIsolation
    List<DepartmentIdpMappingPo> selectByOrganAndIdpType(
        @Param("organId") Long organId,
        @Param("idpType") String idpType
    );
}
