package com.spring.websocket.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{username}")
@Log4j2
public class WebSocket {
    /**
     * 当前在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 以用户名为key，websocket对象为value
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<>();

    /**
     * socket session 会话
     */
    private Session session;

    /**
     * 用户名称
     */
    private String username;

    /**
     * OnOpen 表示有浏览器连接过来的时候被调用
     * OnClose 表示浏览器发出关闭请求的时候被调用
     * OnMessage 表示浏览器发消息的时候被调用
     * OnError 表示有错误发生，比如网络断开了等等被调用
     */

    /**
     * 建立连接
     *
     * @param username
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        onlineNumber++;
        log.info("现在来连接的客户id: " + session.getId() + " 用户名: " + username);
        this.username = username;
        this.session = session;
        log.info("有新连接人数! 当前在线人数: " + onlineNumber);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            //先给所有人推送消息，说我上线了
            Map<String, Object> map = new HashMap<>();
            map.put("messageType", 1);
            map.put("username", username);
            sendMessageAll(JSONObject.toJSONString(map), username);

            //把自己的信息加入到map中
            clients.put(username,this);
            //给自己发送一条消息，告诉自己现在都有谁在线
            Map<String, Object> map2 = new HashMap<>();
            map2.put("messageType", 3);
            //移除掉自己
            Set<String> set = clients.keySet();
            map2.put("onlineUsers", set);
            sendMessageTo(JSON.toJSONString(map2), username);
        } catch (Exception e) {
            log.error(username + "上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        log.error("服务器发生了错误: "+throwable.getMessage());
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(){
        onlineNumber--;
        //将用户清除
        clients.remove(username);
        try {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("messageType",2);
            map1.put("onlineUsers",clients.keySet());
            map1.put("username",username);
            sendMessageAll(JSONObject.toJSONString(map1),username);
        } catch (Exception e) {
            log.error(username + "下线的时候通知所有人发生了错误");
        }
        log.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session){
        try {
            log.info("来自客户的消息: "+message+" 客户端的id是: "+ session.getId());
            JSONObject jsonObject = JSONObject.parseObject(message);
            String textMessage = jsonObject.getString("message");
            String fromUsername = jsonObject.getString("fromUsername");
            String toUsername = jsonObject.getString("toUsername");
            //如果不是发给所有人,则发给指定用户
            // messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            Map<String,Object> map1 = new HashMap<>();
            map1.put("messageType",4);
            map1.put("textMessage",textMessage);
            map1.put("fromUsername",fromUsername);
            if (toUsername.equals("All")){
                map1.put("tousername", "所有人");
                sendMessageAll(JSONObject.toJSONString(map1),toUsername);
            }else {
                map1.put("tousername", toUsername);
                sendMessageTo(JSONObject.toJSONString(map1),toUsername);
            }
        }catch (Exception e){
            log.error("发生了错误了");
        }
    }
    /**
     * 向指定用户发送消息
     * @param message
     * @param toUsername
     */
    private void sendMessageTo(String message, String toUsername) {
        for (WebSocket socket : clients.values()) {
            if (socket.username.equals(toUsername)) {
                socket.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    /**
     * 向所有用户发送消息
     * @param message
     * @param fromUsername
     */
    private void sendMessageAll(String message, String fromUsername) {
        clients.values().forEach(client -> {
            client.session.getAsyncRemote().sendText(message);
        });
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }
}
