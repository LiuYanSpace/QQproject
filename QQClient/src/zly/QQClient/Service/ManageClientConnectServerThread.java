package zly.QQClient.Service;

import java.util.HashMap;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/9 - 01 - 09 - 15:41
 * @Description:
 * @version:1.0
 *
 * 管理客户端连接到服务器端的线程的类
 */
public class ManageClientConnectServerThread {
    //把多个线程放入到集合，key是用户ID，value就是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    //将某线程加入到集合
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread  clientConnectServerThread){
        hm.put(userId,  clientConnectServerThread);
    }

    //通过userId可以得到对应线程
    public static  ClientConnectServerThread getClientConnectServerThread(String userId){
     return hm.get(userId);
    }

}
