package zly.QQClient.Service;

import zly.QQCommon.Message;
import zly.QQCommon.MessageType;
import zly.QQCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/8 - 01 - 08 - 22:45
 * @Description:
 * @version:1.0 客户端
 * 用户登录验证和用户注册等功能
 */
@SuppressWarnings({"all"})
public class UserClientService {

    private User u = new User();//因为我们可能要在其他地方使用User信息，因此作出成员属性
    //因为我们可能要在其他地方使用Socket信息，因此作出成员属性
    private Socket socket;

    //根据ID和密码验证用户是否合法
    public boolean checkUser(String userId, String passWd) {

        boolean b = false;
        //创建User对象
        u.setUserId(userId);
        u.setPassWd(passWd);

        //连接服务端，发送u对象
        try {
            Socket socket = new Socket(InetAddress.getByName("192.168.3.4"), 8888);

            //得到ObjectOutStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(u);//发送u对象

            //读取服务端回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Message ms = (Message) ois.readObject();


            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {//登陆成功


                //创建一个和服务器端保持通讯的线程。创建一个线程类ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动线程
                clientConnectServerThread.start();

                //为了客户端的扩展把线程放在集合中
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);

                b = true;

            } else {

                //登录失败，不能启动和服务器通信的线程
                socket.close();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    //向服务器请求在线用户列表

    public void onlineFriendList() {
        //发送一个message，类型是 MESSAGE_GET_ONLINE_FRIEND ;

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        //得到当前线程的socket对应的 ObjectOutputStream对象

        try {
            /*
            //从管理线程的集合中，通过UserId，得到这个线程
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());

            //通过线程得到关联的socket
            Socket socket = clientConnectServerThread.getSocket();

            //得到当前线程的socket对应的 ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

             */


            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);//发送一个message的对象

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //编写一个方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//一定要指定客户端id

        //发送
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println("用户：" + u.getUserId() + "退出了系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


