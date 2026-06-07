package com.g2rain.department.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 部门树形结构 VO
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "部门树形结构 VO")
public class DepartmentTreeVo {

    /**
     * 全路径编码，如 00010001
     */
    @Schema(description = "全路径编码，如 00010001")
    private String deptPath;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "子部门列表")
    private List<DepartmentTreeVo> children;
}
