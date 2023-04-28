package project.core.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServlet工具类
 *
 * @author tanwei
 * @date 2023-02-06 17:06
 **/
public class HttpServletUtil {


    /**
     * 获取request
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }


    /**
     * 获取response
     *
     * @return 响应对象
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }


    /**
     * 获取ServletRequestAttributes
     *
     * @return ServletRequestAttributes 请求对象
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
}
