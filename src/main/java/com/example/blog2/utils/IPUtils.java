package com.example.blog2.utils;

import com.example.blog2.config.TencentServerConfig;
import com.example.blog2.vo.UserLocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author mxp
 * @date 2023/1/30 10:09
 */
@Component
public class IPUtils {

    private static TencentServerConfig tencentServerConfig;

    @Autowired
    public void setTencentServerConfig(TencentServerConfig tencentServerConfig) {
        IPUtils.tencentServerConfig = tencentServerConfig;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
            if ("127.0.0.1".equals (ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet.getHostAddress();
            }
        }

        if (request.getHeader("X-Real-IP") != null && !"".equals(request.getHeader("X-Real-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("X-Real-IP"))) {
            ip = request.getHeader("X-Real-IP");
        }

        if (request.getHeader("Proxy-Client-IP") != null && !"".equals(request.getHeader("Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("Proxy-Client-IP"))) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (request.getHeader("WL-Proxy-Client-IP") != null && !"".equals(request.getHeader("WL-Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("WL-Proxy-Client-IP"))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static UserLocationVo getUserLocation(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder url = new StringBuilder("https://apis.map.qq.com/ws/location/v1/ip?key=");
        url.append(tencentServerConfig.getKey());
        String u = url.toString();
        if (!StringUtils.isEmpty(ip)) {
            url.append("&ip=").append(ip);
        }
        UserLocationVo object = restTemplate.getForObject(
                url.toString(),
                UserLocationVo.class
        );
        if (object == null || (object.getResult() == null && object.getStatus().equals(375))) {
            return restTemplate.getForObject(
                    u,
                    UserLocationVo.class
            );
        }
        return object;
    }
}
