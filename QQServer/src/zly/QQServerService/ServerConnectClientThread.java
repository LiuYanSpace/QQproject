package zly.QQServerService;

import zly.QQCommon.Message;
import zly.QQCommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/9 - 01 - 09 - 16:39
 * @Description:
 * @version:1.0 各类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {

    //该线程要持有Socket
    private Socket socket;

    private String userId;//连接到服务器端的用户id

    //构造器
    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }


    @Override
    public void run() {
        while (true) {

            try {
                System.out.println("服务器端和客户端" + userId + "保持通信，读取数据-----");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                //判断这个message类型，然后做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端要在线用户列表信息

                    System.out.println(message.getSender() + "要在线用户列表");

                    String onlineUser = ManageServerConnectClientThread.getOnlineUser();
                    //构建一个message对象，返回给客户端
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //返回客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //根据message获取getterId,然后得到对应线程
                    ServerConnectClientThread serverConnectClientThread =
                            ManageServerConnectClientThread.getServerConnectClientThread(message.getGetter());

                    ObjectOutputStream oos =
                            new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());

                    oos.writeObject(message);//转发消息，如果客户离线，可以保存到数据库，实现留言功能



                } else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    //需要遍历管理线程的集合，把所有线程的socket得到，然后把message消息转发
                    HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()){
                        //取出在线用户id
                        String onlineUserId = iterator.next().toString();

                        if(!onlineUserId.equals(message.getSender())){
                            //进行转发
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                        oos.writeObject(message);
                        }
                    }

                }

                else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {//客户端退出

                    System.out.println(message.getSender() + "退出");

                    //将这个客户端对应线程，从集合删除
                    ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    //退出线程
                    break;

                } else {

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

