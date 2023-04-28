package project.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author tanwei
 * @date 2022-11-24 14:29
 **/
@Slf4j
public class ResponseUtil {

    /**
     * IE浏览器
     */
    private static final String MSIE = "MSIE";
    /**
     * 火狐浏览器
     */
    private static final String FIREFOX = "Firefox";
    /**
     * google浏览器
     */
    private static final String CHROME = "Chrome";


    /**
     * 将字符串渲染到客户端
     *
     * @param string 待渲染的字符串
     */
    public static void renderString(String string) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(string);
        } catch (IndexOutOfBoundsException | IOException e) {
            log.error("【将字符串渲染到客户端异常】", e);
        }
    }

    /**
     * 设置下载的响应头信息
     *
     * @param fileName 文件名称
     */
    public static void setDownHeader(String fileName) {
        setDownHeader(HttpServletUtil.getResponse(), fileName);
    }

    /**
     * 设置下载的响应头信息
     *
     * @param response http响应对象
     * @param fileName 文件名称
     */
    public static void setDownHeader(HttpServletResponse response, String fileName) {
        // 清空输出流
        // response.reset();
        // 表明这是一个二进制文件
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 表示不能用浏览器直接打开
        response.setHeader(HttpHeaders.CONNECTION, "close");
        // 让浏览器不缓存
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        // 告诉客户端允许断点续传多线程连接下载
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        // 表明这是一个需要下载的附件并告诉浏览器默认文件名
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodeDownloadFilename(fileName));
    }

    /**
     * 设置下载excel的响应头信息
     *
     * @param fileName 文件名称
     */
    public static void setExcelHeader(String fileName) {
        HttpServletResponse response = HttpServletUtil.getResponse();

        // 表明这是excle xls文件
//        response.setContentType("application/vnd.ms-excel;charset=" + CommonCharConstant.UTF8);
        // 表明这是excle xlsx文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=" + StandardCharsets.UTF_8);
        setDownHeader(response, addColonEnd(fileName, StrUtil.DOT + FileSuffixEnum.XLSX.getOrdinal()));
    }

    /**
     * 设置下载zip的响应头信息
     *
     * @param fileName 文件名
     */
    public static void setZipDownLoadHeader(String fileName) {
        setDownHeader(addColonEnd(fileName, StrUtil.DOT + FileSuffixEnum.ZIP.getOrdinal()));
    }

    /**
     * 下载文件名重新编码
     *
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    private static String encodeDownloadFilename(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return null;
        }

        HttpServletRequest request = HttpServletUtil.getRequest();
        String agent = request.getHeader(HttpHeaders.USER_AGENT);

        // 兼容ie
        if (StrUtil.contains(agent, MSIE)) {
            return encode(fileName).replace("+", " ");
        }

        // 兼容火狐
        if (StrUtil.contains(agent, FIREFOX)) {
            return new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
        }

        // 其它浏览器
        return encode(fileName);
    }

    /**
     * 重新编码
     *
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    private static String encode(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error("【编码异常】文件名：{} 编码：{} 错误信息：{}", fileName, StandardCharsets.UTF_8, e.getMessage());
            return fileName;
        }
    }

    /**
     * 添加指定字符串以 XXX 结尾
     *
     * @param val 字符串
     * @return String 以 XXX 结尾的字符串
     */
    public static String addColonEnd(String val, String endVal) {
        if (val.endsWith(endVal)) {
            return val;
        }

        return val + endVal;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public enum FileSuffixEnum {

        /**
         * xls
         */
        XLS("xls", "2003版，excel表格"),

        /**
         * xlsx
         */
        XLSX("xlsx", "2007版，excel表格"),

        /**
         * zip压缩文件
         */
        ZIP("zip", "zip压缩文件"),

        /**
         * java
         */
        JAVA("java", "java编程语言文件");


        private final String ordinal;

        private final String name;
    }
}
