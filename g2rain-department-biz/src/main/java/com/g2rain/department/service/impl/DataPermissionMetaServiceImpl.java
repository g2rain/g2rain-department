package com.g2rain.department.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.utils.Strings;
import com.g2rain.common.web.PrincipalContextHolder;
import com.g2rain.department.converter.DataPermissionMetaConverter;
import com.g2rain.department.converter.DataPermissionPolicyConverter;
import com.g2rain.department.dao.DataPermissionFieldDao;
import com.g2rain.department.dao.DepartmentUserRelationDao;
import com.g2rain.department.dao.po.DataPermissionPolicyPo;
import com.g2rain.department.dao.DataPermissionMetaDao;
import com.g2rain.department.dao.DataPermissionModelDao;
import com.g2rain.department.dao.po.DataPermissionFieldPo;
import com.g2rain.department.dao.po.DataPermissionMetaPo;
import com.g2rain.department.dao.po.DataPermissionModelPo;
import com.g2rain.department.dto.DataPermissionFieldSelectDto;
import com.g2rain.department.dto.DataPermissionMetaDto;
import com.g2rain.department.dto.DataPermissionMetaSelectDto;
import com.g2rain.department.dto.DataPermissionModelSelectDto;
import com.g2rain.department.dto.DataPermissionPolicyResolveDto;
import com.g2rain.department.dto.DataPermissionSqlValidateDto;
import com.g2rain.department.dto.DataPermissionWhereFragmentResolveDto;
import com.g2rain.department.dto.UpdateStatusDto;
import com.g2rain.department.enums.CommonStatus;
import com.g2rain.department.enums.DepartmentErrorCode;
import com.g2rain.department.service.DataPermissionMetaService;
import com.g2rain.department.service.support.CommonStatusUpdater;
import com.g2rain.department.service.support.DataPermissionIsolationContext;
import com.g2rain.department.service.support.DataPermissionIsolationMetaAdapter;
import com.g2rain.department.service.support.DataPermissionPolicyCacheBroadcaster;
import com.g2rain.department.service.support.DataPermissionPolicyChangeDetector;
import com.g2rain.department.service.support.DataPermissionPolicyResolveResultConverter;
import com.g2rain.department.service.support.DataPermissionSqlIsolationValidator;
import com.g2rain.department.service.support.DataPermissionWhereFragmentVoSupport;
import com.g2rain.department.vo.DataPermissionMetaVo;
import com.g2rain.department.vo.DataPermissionPolicyVo;
import com.g2rain.department.vo.DataPermissionSqlValidateVo;
import com.g2rain.department.vo.DataPermissionWhereFragmentVo;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限元数据表服务实现类
 * 表名: data_permission_meta
 *
 * @author G2rain Generator
 */
@Service(value = "dataPermissionMetaServiceImpl")
public class DataPermissionMetaServiceImpl implements DataPermissionMetaService {

    @Resource(name = "dataPermissionMetaDao")
    private DataPermissionMetaDao dataPermissionMetaDao;

    @Resource(name = "dataPermissionModelDao")
    private DataPermissionModelDao dataPermissionModelDao;

    @Resource(name = "dataPermissionFieldDao")
    private DataPermissionFieldDao dataPermissionFieldDao;

    @Resource(name = "departmentUserRelationDao")
    private DepartmentUserRelationDao departmentUserRelationDao;

    @Resource
    private DataPermissionPolicyCacheBroadcaster policyCacheBroadcaster;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<DataPermissionMetaVo> selectList(DataPermissionMetaSelectDto selectDto) {
        return dataPermissionMetaDao.selectList(selectDto)
            .stream()
            .map(DataPermissionMetaConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<DataPermissionMetaVo> selectPage(PageSelectListDto<DataPermissionMetaSelectDto> selectDto) {
        Page<DataPermissionMetaPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            dataPermissionMetaDao.selectList(selectDto.getQuery());
        });
        List<DataPermissionMetaVo> result = page.getResult()
            .stream()
            .map(DataPermissionMetaConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public DataPermissionPolicyVo resolveDataPermissionPolicy(DataPermissionPolicyResolveDto resolveDto) {
        Asserts.isTrue(Objects.nonNull(resolveDto.getOrganId()), SystemErrorCode.PARAM_REQUIRED, "organId");
        Asserts.isTrue(Objects.nonNull(resolveDto.getUserId()), SystemErrorCode.PARAM_REQUIRED, "userId");
        Asserts.isTrue(Strings.isNotBlank(resolveDto.getDeptPaths()), SystemErrorCode.PARAM_REQUIRED, "deptPaths");
        Asserts.isTrue(Strings.isNotBlank(resolveDto.getModuleCode()), SystemErrorCode.PARAM_REQUIRED, "moduleCode");
        Asserts.isTrue(Strings.isNotBlank(resolveDto.getTableName()), SystemErrorCode.PARAM_REQUIRED, "tableName");

        Set<String> deptPathSet = Arrays.stream(resolveDto.getDeptPaths().split(","))
            .map(String::trim)
            .filter(Strings::isNotBlank)
            .collect(Collectors.toSet());
        Asserts.isTrue(Collections.isNotEmpty(deptPathSet), SystemErrorCode.PARAM_REQUIRED, "deptPaths");
        resolveDto.setDeptPathSet(deptPathSet);
        DataPermissionPolicyPo policyPo = dataPermissionMetaDao.resolveDataPermissionPolicy(resolveDto);
        if (Objects.isNull(policyPo)) {
            return null;
        }

        return DataPermissionPolicyConverter.INSTANCE.po2vo(policyPo);
    }

    @Override
    public List<DataPermissionWhereFragmentVo> resolveWhereFragments(DataPermissionWhereFragmentResolveDto resolveDto) {
        IdentityContext identity = requireIdentity();
        DataPermissionMetaSelectDto selectDto = new DataPermissionMetaSelectDto();
        selectDto.setOrganId(identity.organId());
        selectDto.setStatus(CommonStatus.ACTIVE.name());

        String filterModuleCode = Objects.isNull(resolveDto) ? null : resolveDto.getModuleCode();
        String filterTableName = Objects.isNull(resolveDto) ? null : normalizeTableName(resolveDto.getTableName());
        Map<String, DataPermissionWhereFragmentVo> fragments = new LinkedHashMap<>();

        for (DataPermissionMetaPo metaPo : dataPermissionMetaDao.selectList(selectDto)) {
            DataPermissionModelPo modelPo = dataPermissionModelDao.selectById(metaPo.getModelId());
            if (Objects.isNull(modelPo)) {
                continue;
            }
            String moduleCode = modelPo.getModuleCode();
            if (StringUtils.hasText(filterModuleCode) && !filterModuleCode.equals(moduleCode)) {
                continue;
            }
            String tableName = normalizeTableName(modelPo.getTableName());
            if (StringUtils.hasText(filterTableName) && !filterTableName.equals(tableName)) {
                continue;
            }
            String fragmentKey = moduleCode + "|" + tableName;
            if (fragments.containsKey(fragmentKey)) {
                continue;
            }

            DataPermissionIsolationContext context = resolveIsolationContext(
                moduleCode, tableName, identity
            );
            if (Objects.isNull(context) || Objects.isNull(context.getPolicy())) {
                continue;
            }

            Table table = new Table(tableName);
            DataPermissionWhereFragmentVo vo = new DataPermissionWhereFragmentVo();
            vo.setModuleCode(moduleCode);
            vo.setTableName(tableName);
            DataPermissionWhereFragmentVoSupport.enrich(
                vo,
                context.getPolicy(),
                context.isInGroup(),
                context.getDeptPathsCsv(),
                table,
                context.getIsolationMeta()
            );
            fragments.put(fragmentKey, vo);
        }
        return new ArrayList<>(fragments.values());
    }

    @Override
    public DataPermissionSqlValidateVo validateSql(DataPermissionSqlValidateDto validateDto) {
        Asserts.isTrue(Strings.isNotBlank(validateDto.getModuleCode()), SystemErrorCode.PARAM_REQUIRED, "moduleCode");
        Asserts.isTrue(Strings.isNotBlank(validateDto.getSql()), SystemErrorCode.PARAM_REQUIRED, "sql");

        IdentityContext identity = requireIdentity();
        return DataPermissionSqlIsolationValidator.validate(
            validateDto.getSql(),
            validateDto.getModuleCode(),
            identity.deptPathsCsv(),
            tableName -> resolveIsolationContext(validateDto.getModuleCode(), tableName, identity)
        );
    }

    private DataPermissionIsolationContext resolveIsolationContext(
        String moduleCode,
        String tableName,
        IdentityContext identity
    ) {
        if (!StringUtils.hasText(moduleCode) || !StringUtils.hasText(tableName)) {
            return null;
        }

        DataPermissionModelSelectDto modelSelectDto = new DataPermissionModelSelectDto();
        modelSelectDto.setModuleCode(moduleCode);
        modelSelectDto.setTableName(normalizeTableName(tableName));
        List<DataPermissionModelPo> models = dataPermissionModelDao.selectList(modelSelectDto);
        if (models.isEmpty()) {
            return null;
        }

        DataPermissionModelPo modelPo = models.getFirst();
        DataPermissionMetaSelectDto metaSelectDto = new DataPermissionMetaSelectDto();
        metaSelectDto.setOrganId(identity.organId());
        metaSelectDto.setModelId(modelPo.getId());
        metaSelectDto.setStatus(CommonStatus.ACTIVE.name());
        List<DataPermissionMetaPo> metas = dataPermissionMetaDao.selectList(metaSelectDto);
        if (metas.isEmpty()) {
            return null;
        }

        DataPermissionFieldSelectDto fieldSelectDto = new DataPermissionFieldSelectDto();
        fieldSelectDto.setModelId(modelPo.getId());
        List<DataPermissionFieldPo> fields = dataPermissionFieldDao.selectList(fieldSelectDto);

        DataPermissionPolicyResolveDto policyResolveDto = new DataPermissionPolicyResolveDto();
        policyResolveDto.setOrganId(identity.organId());
        policyResolveDto.setUserId(identity.userId());
        policyResolveDto.setDeptPaths(identity.deptPathsCsv());
        policyResolveDto.setModuleCode(moduleCode);
        policyResolveDto.setTableName(normalizeTableName(tableName));
        DataPermissionPolicyVo policyVo = resolveDataPermissionPolicy(policyResolveDto);

        DataPermissionIsolationContext context = new DataPermissionIsolationContext();
        context.setOrganId(identity.organId());
        context.setUserId(identity.userId());
        context.setDeptPathsCsv(identity.deptPathsCsv());
        context.setMetaPo(metas.getFirst());
        context.setModelPo(modelPo);
        context.setFields(fields);
        context.setIsolationMeta(DataPermissionIsolationMetaAdapter.toMeta(modelPo, fields));
        context.setPolicy(DataPermissionPolicyResolveResultConverter.fromVo(policyVo));
        context.setInGroup(Objects.nonNull(policyVo) && policyVo.isInGroup());
        return context;
    }

    private IdentityContext requireIdentity() {
        Long organId = PrincipalContextHolder.getOrganId();
        Long userId = PrincipalContextHolder.getUserId();
        Asserts.isTrue(Objects.nonNull(organId) && Objects.nonNull(userId),
            DepartmentErrorCode.DATA_PERMISSION_IDENTITY_REQUIRED);

        List<String> deptPaths = departmentUserRelationDao.selectDeptPaths(organId, userId);
        String deptPathsCsv = deptPaths.stream()
            .filter(Strings::isNotBlank)
            .map(String::trim)
            .collect(Collectors.joining(","));
        Asserts.isTrue(Strings.isNotBlank(deptPathsCsv), SystemErrorCode.PARAM_REQUIRED, "deptPaths");
        return new IdentityContext(organId, userId, deptPathsCsv);
    }

    private static String normalizeTableName(String tableName) {
        if (!StringUtils.hasText(tableName)) {
            return tableName;
        }
        return tableName.trim().replace("`", "").toLowerCase(Locale.ROOT);
    }

    private record IdentityContext(Long organId, Long userId, String deptPathsCsv) {
    }

    @Override
    public Long save(DataPermissionMetaDto dto) {
        Long modelId = dto.getModelId();
        DataPermissionModelPo model = dataPermissionModelDao.selectById(modelId);
        Asserts.isTrue(Objects.nonNull(model), DepartmentErrorCode.DATA_PERMISSION_MODEL_NOT_FOUND);

        DataPermissionMetaSelectDto selectDto = new DataPermissionMetaSelectDto();
        selectDto.setOrganId(dto.getOrganId());
        selectDto.setModelId(modelId);
        Long currentId = dto.getId();
        boolean duplicated = dataPermissionMetaDao.selectList(selectDto).stream()
            .anyMatch(item -> !Objects.equals(item.getId(), currentId));
        Asserts.isTrue(!duplicated, DepartmentErrorCode.DATA_PERMISSION_META_MODEL_DUPLICATE);

        DataPermissionMetaPo entity = DataPermissionMetaConverter.INSTANCE.dto2po(dto);

        Long id = entity.getId();
        DataPermissionMetaPo before = Objects.nonNull(id) && id > 0 ? dataPermissionMetaDao.selectById(id) : null;
        if (Objects.isNull(id) || id == 0) {
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            entity.setStatus(CommonStatus.ACTIVE.name());
            int success = dataPermissionMetaDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
            policyCacheBroadcaster.broadcastByMeta(entity);
        } else {
            entity.setUpdateTime(Moments.now());
            int success = dataPermissionMetaDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
            if (DataPermissionPolicyChangeDetector.metaAffecting(before, entity)) {
                policyCacheBroadcaster.broadcastMetaChange(before, entity);
            }
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        DataPermissionMetaPo meta = dataPermissionMetaDao.selectById(id);
        int deleted = dataPermissionMetaDao.delete(id);
        if (deleted > 0) {
            policyCacheBroadcaster.broadcastByMeta(meta);
        }
        return deleted;
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        DataPermissionMetaPo before = dataPermissionMetaDao.selectById(id);
        int updated = CommonStatusUpdater.update(
            id,
            dto,
            () -> {
                DataPermissionMetaPo entity = dataPermissionMetaDao.selectById(id);
                return entity == null ? null : entity.getStatus();
            },
            () -> {
                DataPermissionMetaPo entity = new DataPermissionMetaPo();
                entity.setId(id);
                entity.setStatus(dto.getStatus());
                entity.setUpdateTime(Moments.now());
                return dataPermissionMetaDao.update(entity);
            },
            "dataPermissionMeta"
        );
        if (updated > 0 && Objects.nonNull(before) && !Objects.equals(before.getStatus(), dto.getStatus())) {
            policyCacheBroadcaster.broadcastByMeta(before);
        }
        return updated;
    }
}
