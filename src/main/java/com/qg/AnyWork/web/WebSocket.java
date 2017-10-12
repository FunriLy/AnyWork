package com.qg.AnyWork.web;

import com.qg.AnyWork.config.GetHttpSessionConfigurator;
import com.qg.AnyWork.exception.user.UserNotLoginException;
import com.qg.AnyWork.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by FunriLy on 2017/10/4.
 * From small beginnings comes great things.
 */
//@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
public class WebSocket {

    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    // 记录当前连接数
    private static int onlineCount = 0;
    // 存放服务端与客户端的唯一标识符
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<WebSocket>();

    private Session session;            // 与客户端的会话连接
    private int userid;                 // 用户id
    private HttpSession httpSession;    // 浏览器Session

    //用户id和websocket的session绑定的路由表
    private static Map<Integer, Session> routetab = new HashMap<Integer, Session>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        webSockets.add(this);
        addOnlineCount();
        // 获取当前用户的httpsession
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        try {
            this.userid = ((User)httpSession.getAttribute("user")).getUserId();
        } catch (Exception e) {
            logger.warn("用户连接WebSocket发生错误 ：" , e);
            throw new UserNotLoginException("用户登录发生错误！");
        }
        //绑定用户id与session会话
        routetab.put(userid, session);
        System.out.println("有新链接加入，当前人数为 "+getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        subOnlineCount();
        routetab.remove(userid);
        System.out.println("连接断开，当前人数为 "+getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket 发生错误：");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message) {

    }


    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public HttpSession getHttpSession(){
        return this.httpSession;
    }
}
