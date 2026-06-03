package com.g2rain.department.controller;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.department.api.ContractApi;
import com.g2rain.department.dto.ContractDto;
import com.g2rain.department.dto.ContractSelectDto;
import com.g2rain.department.service.ContractService;
import com.g2rain.department.vo.ContractVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户表控制器
 * 表名: contract
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/contract")
public class ContractController implements ContractApi {

    @Resource(name = "contractServiceImpl")
    private ContractService contractService;

    @Override
    public Result<List<ContractVo>> selectList(ContractSelectDto selectDto) {
        return Result.success(contractService.selectList(selectDto));
    }

    @Override
    public Result<PageData<ContractVo>> selectPage(PageSelectListDto<ContractSelectDto> selectDto) {
        return Result.successPage(contractService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新客户表信息", description = "新增或更新客户表基础信息")
    public Result<Long> save(@RequestBody ContractDto dto) {
        return Result.success(contractService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户表记录", description = "根据主键删除客户表记录")
    public Result<Integer> delete(@Parameter(description = "客户表标识") @PathVariable Long id) {
        return Result.success(contractService.delete(id));
    }
}