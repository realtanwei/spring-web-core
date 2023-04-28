package project.core.constants;


/**
 * 返回状态码 - 枚举
 *
 * @author tanwei
 * @date 2022-11-24 08:52:27
 */
public enum CommonHttpStatusEnum implements BaseExceptionEnum {

    /**
     * 操作成功
     */
    OK(200, "操作成功"),

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    BAD_REQUEST(400, "无效的请求"),

    /**
     * 未认证, 认证超时
     */
    UNAUTHORIZED(401, "未认证或已失效"),

    /**
     * 已认证，但没有该权限
     */
    FORBIDDEN(403, "访问受限，没有该权限"),

    /**
     * 不允许的http方法
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求超时
     */
    REQUEST_TIMED_OUT(408, "请求超时"),

    /**
     * 不支持的数据，媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的数据，媒体类型"),

    /**
     * 热点限流
     */
    TOO_MANY_REQUESTS(429, "请求频率过高，请稍后访问"),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),

    /**
     * 接口未实现
     */
    NOT_IMPLEMENTED(501, "接口未实现"),

    /**
     * 服务器不可用
     */
    SERVICE_UNAVAILABLE(503, "服务器不可用"),

    /**
     * 数据已删除异常
     */
    DATA_DELETED(221, "数据已删除"),


    /* 以下为自定义状态码 */


    /**
     * 默认业务异常（以下为自定义状态码）
     */
    DEFAULT_BUSINESS_EXCEPTION(10, "业务异常"),

    /**
     * 系统授权失效
     */
    SYSTEM_LICENSE_INVALID(11, "系统授权失效"),

    /**
     * 非法操作超级管理员异常
     */
    ILLEGAL_OPERATION_ADMIN_EXCEPTION(12, "非法操作超级管理员"),

    /**
     * 非法请求异常（试图越过系统权限、必须的参数为空，被串改等）
     */
    ILLEGAL_REQUEST_EXCEPTION(13, "非法请求"),


    /**
     * 无效的数据访问API使用异常
     */
    INVALID_DATA_ACCESS_API(14, "无效的数据访问API使用"),
    /**
     * 请求头未检测到GatewayTenantId
     */
    NO_TENANT_ID(15, "请求头未检测到GatewayTenantId");


    /**
     * 提示信息
     */
    private final String message;

    /**
     * 业务响应码
     */
    private final Integer code;

    CommonHttpStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
