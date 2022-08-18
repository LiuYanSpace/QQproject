package zly.QQServerService;

import com.sun.corba.se.spi.activation.Server;
import zly.QQCommon.Message;
import zly.QQCommon.MessageType;
import zly.QQCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/9 - 01 - 09 - 16:17
 * @Description:
 * @version:1.0 服务端监听9999，等待客户端连接，并保持通讯
 */
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法的
    private static HashMap<String, User> ValidUsers = new HashMap<>();

    static {//在静态代码块，初始化ValidUsers
        ValidUsers.put("100", new User("100", "123456"));
        ValidUsers.put("200", new User("200", "123456"));
        ValidUsers.put("300", new User("300", "123456"));
        ValidUsers.put("至尊宝", new User("至尊宝", "123456"));
        ValidUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        ValidUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }


    //验证用户是否有效的方法
    private boolean checkUser(String userId, String passWd) {
        User user = ValidUsers.get(userId);
        if (user == null) {
            return false;
        }
        if (!user.getPassWd().equals(passWd)) {
            return false;
        }

        return true;
    }

    public QQServer() {

        try {
            System.out.println("服务端在8888端口监听-----");

            ss = new ServerSocket(8888);

            while (true) {//当和某个客户端连接后，会继续监听

                Socket socket = ss.accept();

                //得到socket关联的对象的输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                User u = (User) ois.readObject();//读取客户端发送的User对象

                //得到socket关联的对象的输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


                //创建一个Message对象，准备回复客户端

                Message message = new Message();

                //验证用户是否有效

                if (checkUser(u.getUserId() , u.getPassWd())) {

                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);

                    //将message对象回复客户端
                    oos.writeObject(message);

                    // //创建一个和服务器客户端保持通讯的线程。创建一个线程类ServerConnectClientThread

                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());

                    //线程启动
                    serverConnectClientThread.start();

                    //线程放在集合中
                    ManageServerConnectClientThread.addServerConnectClientThread(u.getUserId(), serverConnectClientThread);


                } else {//登录失败

                    System.out.println("用户ID= " + u.getUserId() + " 密码psw= " + u.getPassWd() + " 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出了while循环，说明服务端不再监听，因此要关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
