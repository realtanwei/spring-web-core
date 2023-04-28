package project.core.exception;


import cn.hutool.core.util.StrUtil;
import project.core.constants.BaseExceptionEnum;
import project.core.constants.CommonHttpStatusEnum;

/**
 * 自定义基础 - 异常
 *
 * @author tanwei
 * @date 2022-11-24 14:14:51
 */
public class BaseException extends RuntimeException {

    /**
     * 业务响应码
     */
    private final Integer code;

    /**
     * 提示信息
     */
    private final String formatMessage;

    /**
     * 填充formatMessage使用的参数
     */
    private final Object[] params;

    public BaseException(Integer code, String message, Object... params) {
        super(message);
        this.code = code;
        this.formatMessage = message;
        this.params = params;
    }

    public BaseException() {
        this(CommonHttpStatusEnum.INTERNAL_SERVER_ERROR);
    }

    public BaseException(String message) {
        this(CommonHttpStatusEnum.DEFAULT_BUSINESS_EXCEPTION, message);
    }

    public BaseException(String message, Object... params) {
        this(CommonHttpStatusEnum.DEFAULT_BUSINESS_EXCEPTION, message, params);
    }

    public BaseException(BaseExceptionEnum baseError) {
        this(baseError.getCode(), baseError.getMessage());
    }

    public BaseException(BaseExceptionEnum baseError, String message) {
        this(baseError.getCode(), message);
    }

    public BaseException(BaseExceptionEnum baseError, String message, Object... params) {
        this(baseError.getCode(), message, params);
    }


    public Integer getCode() {
        return code;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public String getMessage() {
        return StrUtil.format(formatMessage, params);
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

}
