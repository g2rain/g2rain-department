package com.g2rain.department.api;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.dto.ContractSelectDto;
import com.g2rain.department.vo.ContractVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 客户表API接口
 * 表名: contract
 *
 * @author G2rain Generator
 */
@Tag(name = "客户表", description = "客户表相关接口")
public interface ContractApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询客户表列表", description = "根据查询条件返回客户表列表")
    Result<List<ContractVo>> selectList(ContractSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询客户表列表", description = "分页查询客户表列表")
    Result<PageData<ContractVo>> selectPage(PageSelectListDto<ContractSelectDto> selectDto);
}