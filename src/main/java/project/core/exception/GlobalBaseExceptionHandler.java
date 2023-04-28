package project.core.exception;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.core.constants.CommonHttpStatusEnum;
import project.core.utils.ResultUtil;

/**
 * 基础 - 全局异常处理器
 *
 * @author tanwei
 * @date 2022-11-24 14:14:51
 */
@Slf4j
@RestControllerAdvice
public class GlobalBaseExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BaseException.class)
    public ResultUtil<String> baseException(BaseException e) {
        log.error("【全局异常-业务异常】 {}", e.getMessage());
        if (e.getCode() != null) {
            return ResultUtil.error(e.getMessage(), e.getCode());
        }
        return ResultUtil.error(e.getMessage());
    }

    /**
     * 未捕获异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResultUtil<String> handleException(Exception e) {
        if (StrUtil.contains(e.getMessage(), "com.netflix.client.ClientException")) {
            log.error("【全局异常-无可用服务】 {}", e.getMessage());
            return ResultUtil.error(CommonHttpStatusEnum.SERVICE_UNAVAILABLE);
        }

        log.error("【全局异常-未捕获异常】 " + e.getMessage(), e);
        return ResultUtil.error(CommonHttpStatusEnum.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
    }
}
