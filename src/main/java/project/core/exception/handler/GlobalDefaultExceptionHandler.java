package project.core.exception.handler;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import project.core.constants.CommonHttpStatusEnum;
import project.core.utils.ResultUtil;

import javax.servlet.ServletException;
import javax.validation.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认 - 全局异常处理器
 *
 * @author tanwei
 * @date 2022-11-24 14:14:51
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * 空指针
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultUtil<String> handlerNullPointerException(NullPointerException e) {
        log.error("【全局异常-空指针】 {}", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 路径不存在
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResultUtil<String> handlerNoFoundException(NoHandlerFoundException e) {
        log.error("【全局异常-资源不存在】 {} {}", e.getMessage(), e.getRequestURL());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 路径不存在
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultUtil<String> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("【全局异常-请求方法不存在】 {} ", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 非法参数异常
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultUtil<String> handlerIllegalArgumentException(IllegalArgumentException e) {
        log.error("【全局异常-非法参数异常】 {}", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST + ": " + e.getMessage());
    }

    /**
     * 请求参数转换错误
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResultUtil<String> handlerHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("【全局异常-请求参数转换错误】 {}", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 请求参数转换异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResultUtil<String> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("【全局异常-请求参数转换异常】 {}", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 数据绑定异常
     */
    @ExceptionHandler(value = BindException.class)
    public ResultUtil<String> validatedBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(objectError -> objectError.getField() + objectError.getDefaultMessage())
                .collect(Collectors.joining(StrUtil.COMMA));
        log.error("【全局异常-数据绑定异常】 {}", message);
        return ResultUtil.error(message, CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 获取@Validated（spring对hibernate validator的扩展用于校验基础数据类型）注解抛出来的错误
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResultUtil<String> validatedExceptionHandler(ValidationException e) {
        String error = null;
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            StringBuilder buff = new StringBuilder();
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                buff.append(item.getPropertyPath()).append(StrUtil.COLON).append(item.getMessage()).append(StrUtil.COMMA);
            }
            error = buff.substring(0, buff.length() - 1);
        } else if (e instanceof ConstraintDeclarationException) {
            // HV000151 问题
            String solveScheme = "To solve the issue, add the constraints to the interface method instead of the implementation method.";
            error = e.getMessage() + StrUtil.COMMA + solveScheme;
        }
        log.error("【全局异常-方法参数验证异常】 {}", error);
        return ResultUtil.error(error, CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 获取@valid，注解抛出来的错误
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultUtil<String> validExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(objectError -> objectError.getField() + objectError.getDefaultMessage())
                .collect(Collectors.joining(StrUtil.COMMA));
        log.error("【全局异常-方法参数验证异常】 {}", message);
        return ResultUtil.error(message, CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 必须的参数不存在
     */
    @ExceptionHandler(value = {MissingServletRequestPartException.class, MissingServletRequestParameterException.class})
    public ResultUtil<String> validExceptionHandler(ServletException e) {
        log.error("【全局异常-必须的参数不存在】 {}", e.getMessage());
        String parameterName = "";
        if (e instanceof MissingServletRequestPartException) {
            MissingServletRequestPartException exception = (MissingServletRequestPartException) e;
            parameterName = exception.getRequestPartName();
            return  ResultUtil.error( String.format("必须的参数【%s】不存在", parameterName), CommonHttpStatusEnum.BAD_REQUEST);
        }

        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException exception = (MissingServletRequestParameterException) e;
            parameterName = exception.getParameterName();
        }
        return  ResultUtil.error( String.format("必须的参数【%s】不存在", parameterName), CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 请求体不存在, Required request body is missing
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResultUtil<?> validExceptionHandler(HttpMessageNotReadableException e) {
        log.error("【全局异常-请求体不存在】 {}", e.getMessage());
        return ResultUtil.error("接口请求体不存在", CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 请求体参数类型不匹配, Cannot deserialize value of type
     */
    @ExceptionHandler(value = MismatchedInputException.class)
    public ResultUtil<?> validExceptionHandler(MismatchedInputException e) {
        log.error("【全局异常-请求体参数类型错误】 {}", e.getMessage());
        return ResultUtil.error("请求体参数类型错误", CommonHttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 不支持类型异常（比如@NotBlank 注解在Long上）
     */
    @ExceptionHandler(value = UnexpectedTypeException.class)
    public ResultUtil<String> validExceptionHandler(UnexpectedTypeException e) {
        log.error("【全局异常-不支持类型异常】 {}", e.getMessage());
        return ResultUtil.error(CommonHttpStatusEnum.UNSUPPORTED_MEDIA_TYPE);
    }

}
