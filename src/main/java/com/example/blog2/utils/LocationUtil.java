package com.example.blog2.utils;

import com.example.blog2.config.TencentServerConfig;
import com.example.blog2.vo.UserLocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author mxp
 * @date 2023/2/6 20:58
 */
@Component
public class LocationUtil {

    private static TencentServerConfig tencentServerConfig;

    @Autowired
    public void setTencentServerConfig(TencentServerConfig tencentServerConfig) {
        LocationUtil.tencentServerConfig = tencentServerConfig;
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
