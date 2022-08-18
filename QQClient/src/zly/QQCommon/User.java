package zly.QQCommon;

import java.io.Serializable;

/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/7 - 01 - 07 - 17:30
 * @Description:
 * @version:1.0
 */
@SuppressWarnings({"all"})
public class User implements Serializable {//一定要先序列化
    //为了保障序列化兼容性
    private static final long serialVersionUID = 1L;

    private String userId;
    private String passWd;

    //无参构造器
    public User(){

    }

    public User(String userId, String passWd) {
        this.userId = userId;
        this.passWd = passWd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }
}
