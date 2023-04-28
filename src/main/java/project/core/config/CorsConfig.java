package project.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;

/**
 * 跨域配置
 *
 * @author tanwei
 * @date 2022-11-24 9:11
 **/
@Configuration
public class CorsConfig {

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 是否允许携带cookie
        // config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        // 允许跨域访问的域名，可填写具体域名，*代表允许所有访问
        config.addAllowedOrigin("*");
        // 允许访问类型：get、post 等，*代表所有类型
        config.addAllowedHeader("*");
        config.setMaxAge(Duration.ofHours(9));

        // 对接口配置跨域设置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
