package zly.QQServerService;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/9 - 01 - 09 - 16:55
 * @Description:
 * @version:1.0
 */
public class ManageServerConnectClientThread {
    //把多个线程放入到集合，key是用户ID，value就是线程
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    //返回hashmap
    public static HashMap<String, ServerConnectClientThread> getHm(){
        return hm;
    }


    //将某线程加入到集合
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //增加一个方法，从集合中移除某个线程对象
    public static void removeServerConnectClientThread(String userId){
        hm.remove(userId);
    }


    //通过userId可以得到对应线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //编写一个方法，可以返回在线用户列表
    public static String getOnlineUser(){
        //集合遍历，遍历hashmap的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()){
            onlineUserList += iterator.next().toString() + "";
        }

        return onlineUserList;
    }

}
