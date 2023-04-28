package project.core.utils;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.core.constants.BaseExceptionEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回工具
 *
 * @author tanwei
 * @date 2022/11/24 16:34
 */
@Data
@NoArgsConstructor
public class ResultUtil<D> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private D data;

    /**
     * 状态枚举
     */
    @AllArgsConstructor
    public enum StatusEnum {

        /**
         * 成功
         */
        SUCCESS(200, "操作成功"),
        /**
         * 失败
         */
        ERROR(500, "操作失败");

        @Getter
        private final Integer code;
        private final String message;
    }

    private ResultUtil(D data, String message, Integer code) {
        super();
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <V> ResultUtil<Map<String, V>> successMap() {
        return success(new HashMap<>(16));
    }

    /**
     * data不是map会返回null
     *
     * @param key   key
     * @param value value
     * @param <V>   value泛型
     * @return this
     */
    public <V> ResultUtil<Map<String, V>> put(String key, V value) {
        if (data instanceof Map) {
            ((Map<String, V>) data).put(key, value);
            return (ResultUtil<Map<String, V>>) this;
        }

        return null;
    }

    public static <M> ResultUtil<M> success() {
        return success(null);
    }

    public static <M> ResultUtil<M> success(M data) {
        return success(data, StatusEnum.SUCCESS.message);
    }

    public static <D> ResultUtil<D> success(D data, String message) {
        return ResultUtil.build(data, message, StatusEnum.SUCCESS.code);
    }

    public static <D> ResultUtil<D> error() {
        return error(StatusEnum.ERROR.message, StatusEnum.ERROR.code);
    }

    public static <D> ResultUtil<D> error(String message) {
        return error(message, StatusEnum.ERROR.code);
    }

    public static <D> ResultUtil<D> error(Integer code) {
        return error(StatusEnum.ERROR.message, code);
    }

    public static <D> ResultUtil<D> error(String message, Integer code) {
        return ResultUtil.build(null, message, code);
    }

    public static <D> ResultUtil<D> error(BaseExceptionEnum httpStatusEnum) {
        return build(null, httpStatusEnum.getMessage(), httpStatusEnum.getCode());
    }

    public static <D> ResultUtil<D> error(String message, BaseExceptionEnum httpStatusEnum) {
        return build(null, message, httpStatusEnum.getCode());
    }

    public static <D> ResultUtil<D> error(BaseExceptionEnum httpStatusEnum, Integer code) {
        return build(null, httpStatusEnum.getMessage(), code);
    }

    private static <D> ResultUtil<D> build(D data, String message, Integer code) {
        return new ResultUtil<>(data, message, code);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}