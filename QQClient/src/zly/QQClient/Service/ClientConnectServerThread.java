package zly.QQClient.Service;

import zly.QQCommon.Message;
import zly.QQCommon.MessageType;
import zly.QQCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/9 - 01 - 09 - 15:06
 * @Description:
 * @version:1.0
 */
public class ClientConnectServerThread extends Thread {
    //该线程要持有Socket
    private Socket socket;

    //构造器可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }


    //因为线程需要在后台和服务器通讯，因此用while循环
    @Override
    public void run() {
        while (true) {

           // System.out.println("客户端线程，等待读取从服务器端发送的消息");

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                //判断这个message类型，然后做相应的业务处理
                //如果读取到的是服务端返回的在线用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息，并显示
                    // 规定
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n=========当前用户列表==========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                } else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    //把服务器转发的消息，显示到控制台即可
                    System.out.println("\n" + message.getSender()
                            + "对" +message.getGetter() + "说" + message.getContent());

                }
                else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){

                    System.out.println("\n" + message.getSender()
                            + "对大家说" + message.getContent());

                }

                else {

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //为了更方便得到Socket
    public Socket getSocket() {
        return socket;
    }
}
