package com.beeplay.websocket;

import org.springframework.web.socket.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class SystemWebSocketHandler implements WebSocketHandler {
    private static ConcurrentMap<String,CopyOnWriteArraySet> usersRoom=new ConcurrentHashMap<>();

    private static String[] nikeName={
            "刘备","董卓","曹操","关羽","张飞",
            "赵云","吕布","袁绍","貂蝉","小乔",
            "大乔","司马懿", "孙权","许褚","黄盖",
            "夏侯惇","张辽", "诸葛亮","郭嘉","马超",
            "黄忠","魏延", "张春华","武松","西门庆",
            "潘金莲","孙二娘","西施","东施","宋江"};

    private static ConcurrentMap<String,String> nikeToId=new ConcurrentHashMap<>();

    private String getNikeName(){
        java.util.Random random=new java.util.Random();// 定义随机类
        int result=random.nextInt(nikeName.length-1);// 返回[0,10)集合中的整数，注意不包括10
        return nikeName[result];
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        System.out.println("房间号："+session.getUri().getQuery());
        //拿到参数后做身份认证

        String roomId=session.getUri().getQuery();
        CopyOnWriteArraySet<WebSocketSession> user=usersRoom.get(roomId);
        if(user!=null){
            user.add(session);
        }else {
            //如果没有房间创建一个房间
            CopyOnWriteArraySet<WebSocketSession> user1=new CopyOnWriteArraySet();
            user1.add(session);
            usersRoom.put(roomId,user1);
        }
        //users.add(session);//身份认证后把session加入

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);


        String id=session.getId();
        String nikeName=getNikeName()+"("+id+")";
        nikeToId.put(id,nikeName);

        CopyOnWriteArraySet<WebSocketSession> user2=usersRoom.get(roomId);

        sendMessageToUsers(user2,new TextMessage("****"+nikeName+"****"+"加入聊天室；当前在线人数："+user2.size()+"人("+dateString+")"));
        System.out.println("****"+nikeName+"****"+"加入聊天室；当前在线人数："+user2.size()+"人("+dateString+")");

    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)  {

        String roomId=session.getUri().getQuery();
        CopyOnWriteArraySet<WebSocketSession> user=usersRoom.get(roomId);

        String id=session.getId();
        String nikeName=nikeToId.get(id);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        if(null==message.getPayload()||message.getPayload().equals("")) {
            sendMessageToUsers(user,new TextMessage("系统：用户["+nikeName+ "]" + "竟然一个字都懒得打！大家快鄙视他！(" + dateString + ")"));
        }else {
            sendMessageToUsers(user,new TextMessage("["+nikeName+ "]" + ":" + message.getPayload() + "(" + dateString + ")"));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        if(session.isOpen()){
            try {

                session.close();
            }catch (IOException e){
                System.out.println("关闭出错");
            }
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String id=session.getId();
        String nikeName=nikeToId.get(id);

        String roomId=session.getUri().getQuery();
        CopyOnWriteArraySet<WebSocketSession> user=usersRoom.get(roomId);
        user.remove(session);

        sendMessageToUsers(user,new TextMessage("***"+nikeName+"***"+"退出聊天室；当前在线人数："+user.size()+"人("+dateString+")"));
        System.out.println("****"+nikeName+"****"+"退出聊天室；当前在线人数："+user.size()+"人("+dateString+")");

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

        String roomId=session.getUri().getQuery();
        CopyOnWriteArraySet<WebSocketSession> user=usersRoom.get(roomId);
        user.remove(session);

        if(user.size()==0){
            usersRoom.remove("roomId");
        }

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String id=session.getId();
        String nikeName=nikeToId.get(id);
        sendMessageToUsers(user,new TextMessage("***"+nikeName+"***"+"退出聊天室；当前在线人数："+user.size()+"人("+dateString+")"));
        System.out.println("****"+nikeName+"****"+"加入聊天室；当前在线人数："+user.size()+"人("+dateString+")");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给房间内用户发送消息
     * @param message
     */
    public void sendMessageToUsers(CopyOnWriteArraySet<WebSocketSession> users,TextMessage message) {

        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}