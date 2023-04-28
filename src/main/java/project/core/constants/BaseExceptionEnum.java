package project.core.constants;


/**
 * 不需要对异常类型捕获区分的话，直接使用枚举实现
 *
 * @author tanwei
 * @date 2022-11-24 08:52:27
 */
public interface BaseExceptionEnum {

    /**
     * 异常信息
     *
     * @return 异常信息
     */
    String getMessage();

    /**
     * 默认异常状态
     *
     * @return 状态码
     */
    default Integer getCode() {
        return CommonHttpStatusEnum.DEFAULT_BUSINESS_EXCEPTION.getCode();
    }

}
