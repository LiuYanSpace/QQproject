package zly.QQCommon;

import java.io.Serializable;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/7 - 01 - 07 - 17:30
 * @Description:
 * @version:1.0
 *
 * 表示消息对象
 */
@SuppressWarnings({"all"})
public class Message implements Serializable {
    //为了保障序列化兼容性
    private static final long serialVersionUID = 1L;
    private String sender;
    private String getter;
    private String content;
    private String sendTime;
    private String mesType;//消息类型：在接口中定义消息类型

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
