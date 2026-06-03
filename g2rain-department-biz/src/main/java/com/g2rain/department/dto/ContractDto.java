package com.g2rain.department.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 客户表查询DTO
 * 表名: contract
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户表 DTO")
public class ContractDto extends BaseDto {

    /**
     * 租户/机构标识
     */
    @Schema(description = "租户/机构标识")
    private Long organId;

    /**
     * 
     */
    @Schema(description = "")
    private Long userId;

    /**
     * 
     */
    @Schema(description = "")
    private String deptPath;

    /**
     * 
     */
    @Schema(description = "")
    private String contractNo;

    /**
     * 
     */
    @Schema(description = "")
    private Long contractId;

    /**
     * 状态[ACTIVE:有效, INACTIVE:无效]
     */
    @Schema(description = "状态[ACTIVE:有效, INACTIVE:无效]")
    private String status;
}