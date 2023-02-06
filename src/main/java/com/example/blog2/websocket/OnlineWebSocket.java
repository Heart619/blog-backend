package com.example.blog2.websocket;

import com.example.blog2.utils.LocationUtil;
import com.example.blog2.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mxp
 * @date 2023/2/5 19:48
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/online")
public class OnlineWebSocket {

    private static final AtomicLong ONLINE_USER = new AtomicLong(0L);
    private String ip;
    private Session session;
    private static final ConcurrentHashMap<String, OnlineWebSocket> SESSION_ID_TO_WEBSOCKET = new ConcurrentHashMap<>();

    @OnOpen
    public void onConnection(Session session) throws IOException {
        String tmpIp = getRemoteAddress(session).getHostString();
        this.session = session;

        UserLocationVo userLocation = LocationUtil.getUserLocation(tmpIp);
        UserLocationVo.Result result = userLocation.getResult();
        this.ip = result.getIp();
        if (!SESSION_ID_TO_WEBSOCKET.containsKey(this.ip)) {
            ONLINE_USER.incrementAndGet();
            UserLocationVo.Ad_info ad_info = result.getAd_info();
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

    public static InetSocketAddress getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();
        //在Tomcat 8.0.x版本有效
//        InetSocketAddress addr0 = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
//        System.out.println("clientIP0" + addr0);
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        return addr;
    }


    private static Object getFieldInstance(Object obj, String fieldPath) {
        String fields[] = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
            }
        }

        return null;
    }

}