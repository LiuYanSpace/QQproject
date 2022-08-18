package zly.QQClient.Service;

import zly.QQClient.Utils.Utility;
import zly.QQCommon.Message;
import zly.QQCommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/10 - 01 - 10 - 17:22
 * @Description:
 * @version:1.0 提供和消息相关的服务方法
 */
public class MessageClientService {
    public void sendMessageToAll(String content, String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(senderId);

        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + "对大家说" + content);


        //准备发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sendMessageToOne(String content, String senderId, String getterId) {

        //构建message对象
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + "对" + getterId + "说" + content);


        //准备发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
