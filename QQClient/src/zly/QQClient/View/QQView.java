package zly.QQClient.View;

import zly.QQClient.Service.MessageClientService;
import zly.QQClient.Service.UserClientService;
import zly.QQClient.Utils.Utility;
import zly.QQCommon.Message;


/**
 * @Auther:ZhengLiuYan
 * @Date: 2022/1/7 - 01 - 07 - 18:02
 * @Description:
 * @version:1.0
 */
@SuppressWarnings({"all"})
public class QQView {
    private boolean loop = true;//控制是否显示菜单

    private String key = "";//接收用户的键盘输入


    private UserClientService userClientService = new UserClientService();//用于登录服务器、注册用户

    private MessageClientService messageClientService = new MessageClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
    }

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("========欢迎登录========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");

            System.out.println("请输入你的选择");

            key = Utility.readString(1);

            switch (key) {//根据用户的输入，来处理不同的逻辑

                case "1":
                    System.out.println("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码");
                    String passWd = Utility.readString(50);
                    //需要到服务端验证该用户是否合法
                    //

                    if (userClientService.checkUser(userId, passWd)) {
                        System.out.println("========欢迎" + userId + "登录成功========");
                        //进入二级菜单
                        while (loop) {
                            System.out.println("=======二级菜单" + userId + "========");
                            System.out.println("1 显示在线用户列表");
                            System.out.println("2 群发消息");
                            System.out.println("3 私聊消息");
                            System.out.println("4 发送文件");
                            System.out.println("9 退出系统");

                            System.out.println("请输入你的选择");

                            key = Utility.readString(1);

                            switch (key) {//根据用户的输入，来处理不同的逻辑

                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话");
                                    String s = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务器
                                    messageClientService.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想要聊天的用户号：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String  content= Utility.readString(100);
                                    //编写一个方法，将消息发送给服务器
                                    messageClientService.sendMessageToOne(content, userId, getterId);


                                    break;
                                case "4":
                                    System.out.println("4 发送文件");
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;

                            }
                        }
                    } else {
                        System.out.println("登录失败");
                    }

                    break;

                case "9":
                    loop = false;
                    break;
            }

        }
    }
}
