package com.example.blog2.websocket;

import com.example.blog2.config.WebSocketConfig;
import com.example.blog2.utils.IPUtils;
import com.example.blog2.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mxp
 * @date 2023/2/5 19:48
 */

@Slf4j
@Component
@ServerEndpoint(value = "/websocket/online", configurator = WebSocketConfig.class)
public class OnlineWebSocket {

    private static final AtomicLong ONLINE_USER = new AtomicLong(0L);
    private String ip;
    private Session session;
    private static final ConcurrentHashMap<String, OnlineWebSocket> SESSION_ID_TO_WEBSOCKET = new ConcurrentHashMap<>();

    @OnOpen
    public void onConnection(Session session) throws IOException {
        this.session = session;
        Map<String, Object> userProperties = session.getUserProperties();
        UserLocationVo userLocation = IPUtils.getUserLocation((String) userProperties.get(WebSocketConfig.IP_ADDR));

        this.ip = userLocation.getResult().getIp();
        UserLocationVo.Ad_info ad_info = userLocation.getResult().getAd_info();
        if (!SESSION_ID_TO_WEBSOCKET.containsKey(this.ip)) {
            ONLINE_USER.incrementAndGet();
            log.info("~Welcome~ IP：[{}] 位于[{}-{}-{}-{}]访问", this.ip, ad_info.getNation(), ad_info.getProvince(), ad_info.getCity(), ad_info.getDistrict());
        }
        SESSION_ID_TO_WEBSOCKET.put(this.ip, this);
        sendOnlineTotal();
    }

    private void sendOnlineTotal() throws IOException {
        for (Map.Entry<String, OnlineWebSocket> entry : SESSION_ID_TO_WEBSOCKET.entrySet()) {
            OnlineWebSocket socket = entry.getValue();
            if (socket.session.isOpen()) {
                socket.session.getBasicRemote().sendText(String.valueOf(ONLINE_USER.get()));
            }
        }
    }

    @OnClose
    public void onClose() throws IOException {
        if (SESSION_ID_TO_WEBSOCKET.containsKey(this.ip)) {
            SESSION_ID_TO_WEBSOCKET.remove(this.ip);
            ONLINE_USER.decrementAndGet();
        }
        sendOnlineTotal();
    }

    @OnError
    public void onError(Throwable error) {
    }

}
