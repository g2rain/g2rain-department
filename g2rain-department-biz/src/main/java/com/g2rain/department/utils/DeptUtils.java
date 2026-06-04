package com.g2rain.department.utils;

import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.utils.Strings;

import java.util.Objects;

/**
 * 部门路径编码工具类。
 * <p>
 * deptPath 按层级叠加，每层固定 4 位 36 进制段，例如：
 * 一级 0001、0002；二级 00010001、00020001；三级 000100010001。
 * </p>
 */
public final class DeptUtils {

    public static final Long ROOT_PARENT_ID = 0L;

    public static final String SYNC_OUTPUT_BINDING = "output";

    public static final String DATA_PERMISSION_POLICY_SOURCE = "DATA_PERMISSION_POLICY";

    private static final int PATH_SEGMENT_WIDTH = 4;

    private static final int PATH_SEGMENT_RADIX = 36;

    private static final String FIRST_PATH_SEGMENT = "0001";

    private DeptUtils() {
    }

    /**
     * 根据父路径与同级最大 deptPath，生成下一个 deptPath。
     *
     * @param parentDeptPath     父部门 deptPath，根节点下为空串
     * @param maxSiblingDeptPath 同级已有最大 deptPath，无同级时为 null
     */
    public static String nextDeptPath(String parentDeptPath, String maxSiblingDeptPath) {
        String parentPrefix = Objects.requireNonNullElse(parentDeptPath, "");
        String maxSegment = extractSiblingSegment(parentPrefix, maxSiblingDeptPath);
        String nextSegment = nextSegmentAfter(maxSegment);
        return buildDeptPath(parentPrefix, nextSegment);
    }

    /**
     * 从同级最大 deptPath 中提取当前层级的 4 位路径段。
     */
    static String extractSiblingSegment(String parentDeptPath, String maxSiblingDeptPath) {
        if (Strings.isBlank(maxSiblingDeptPath)) {
            return null;
        }

        String parentPrefix = Objects.requireNonNullElse(parentDeptPath, "");
        if (!maxSiblingDeptPath.startsWith(parentPrefix)) {
            throw new BusinessException(SystemErrorCode.PARAM_INVALID_FORMAT, maxSiblingDeptPath, "与父部门路径前缀不一致");
        }

        String segment = maxSiblingDeptPath.substring(parentPrefix.length());
        if (segment.length() != PATH_SEGMENT_WIDTH) {
            throw new BusinessException(SystemErrorCode.PARAM_INVALID_FORMAT, maxSiblingDeptPath, "4位36进制部门路径段");
        }
        return segment;
    }

    /**
     * 基于同级最大路径段，计算下一个路径段。
     */
    static String nextSegmentAfter(String maxSegment) {
        if (Strings.isBlank(maxSegment)) {
            return FIRST_PATH_SEGMENT;
        }
        return incrementSegment(maxSegment);
    }

    /**
     * 拼接父路径与当前路径段，得到完整 deptPath。
     */
    public static String buildDeptPath(String parentDeptPath, String pathSegment) {
        if (Strings.isBlank(parentDeptPath)) {
            return pathSegment;
        }

        return parentDeptPath + pathSegment;
    }

    private static String incrementSegment(String segment) {
        try {
            int nextValue = Integer.parseInt(segment, PATH_SEGMENT_RADIX) + 1;
            String nextCode = Integer.toString(nextValue, PATH_SEGMENT_RADIX);
            if (nextCode.length() > PATH_SEGMENT_WIDTH) {
                throw new BusinessException(SystemErrorCode.PARAM_EXCEEDS_RANGE, segment, "0001-zzzz");
            }

            return "0".repeat(PATH_SEGMENT_WIDTH - nextCode.length()) + nextCode;
        } catch (NumberFormatException ex) {
            throw new BusinessException(SystemErrorCode.PARAM_INVALID_FORMAT, segment, "4位36进制部门路径段");
        }
    }
}
