package com.qg.AnyWork.utils;

import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.exception.MailSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Created by FunriLy on 2017/8/20.
 * From small beginnings comes great things.
 */
@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String Sender;

    /**
     * 向用户邮箱发送邮件
     * @param email 用户邮箱地址
     * @param userName 用户昵称
     * @param index 标志
     *
     * index == 1 发送用户注册邮件
     * index == 2 发送忘记密码邮件
     */
    public void send(String email, String userName, int index){
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo(email);
            if (index == 1){
                helper.setSubject("主题：用户注册验证");
                helper.setText(registerHtmlText(email, userName), true);
            } else if (index == 2) {
                helper.setSubject("主题：用户忘记密码");
                helper.setText(PasswordhtmlText(email, userName), true);
            } else {
                throw new Exception("未知参数");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MailSendException("发送邮件失败");
        }
        javaMailSender.send(message);
    }

    private static String registerHtmlText(String email, String userName) {
        final String ciphertext = Encryption.getMD5(email);
        String html = null;
        html =
                "<style type=\"text/css\">html,\n" +
                        "    body {\n" +
                        "        margin: 0;\n" +
                        "        padding: 0;\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<center>\n" +
                        "<table style=\"background:#f6f7f2;font-size:13px;font-family:microsoft yahei;\">\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600px\">\n" +
                        "\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t<td align=\"center\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top:120px;background: url(http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/5891eacba5ba41f389168121f08be02f.jpg) no-repeat;\" width=\"538px\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t<td height=\"70px\" valign=\"middle\" width=\"100%\"><img alt=\"logo\" src=\"https://www.tuchuang001.com/images/2017/10/15/1686ccdd7919429a8beeb4f3f15d5eb1.png\" style=\"margin-left:50px;\" /></td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t<td align=\"center\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#fff;height:411px;\" width=\"465px\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#666;line-height:1.5\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"width:360px;text-align:left;margin-top:50px;margin-bottom:80px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p>亲爱的"+"用户 " + userName +"，您好：</p>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-indent:2em\">\n" +
                        "请点击下面的链接完成用户注册(5分钟内有效)：<br />"+
                        "<h1>用户注册</h1><br/>"+
                        "<a href='http://10.21.48.11:8080/"+"anywork/utils/check?email=" +email+"&ciphertext="+ciphertext+"'>点击完成注册</a></p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"margin-bottom:40px;\"><a href=\"http://10.21.48.11:8080/anywork/html/login.html\" style=\"display:inline-block;width:139px;height:38px;line-height:38px;color:#fff;font-size:14px;vertical-align:middle;background:url(http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/0edb116f982044ba85ecd313f20e881c.jpg);text-decoration:none\">AnyWork</a></div>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:1px dashed #ccc;margin:20px\">&nbsp;</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"37px\" style=\"background:#dededc\">&nbsp;</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t</tbody>\n" +
                        "\t\t\t</table>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>\n" +
                        "</center>"
        ;
        return html;
    }


    private static String PasswordhtmlText(String email, String userName) {
        final String ciphertext = Encryption.getMD5(email);
        String html = null;
        html =
                "<style type=\"text/css\">html,\n" +
                        "    body {\n" +
                        "        margin: 0;\n" +
                        "        padding: 0;\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<center>\n" +
                        "<table style=\"background:#f6f7f2;font-size:13px;font-family:microsoft yahei;\">\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600px\">\n" +
                        "\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t<td align=\"center\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top:120px;background: url(http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/5891eacba5ba41f389168121f08be02f.jpg) no-repeat;\" width=\"538px\">\n" +
                        "\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t<td height=\"70px\" valign=\"middle\" width=\"100%\"><img alt=\"logo\" src=\"https://www.tuchuang001.com/images/2017/10/15/1686ccdd7919429a8beeb4f3f15d5eb1.png\" style=\"margin-left:50px;\" /></td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t<td align=\"center\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#fff;height:411px;\" width=\"465px\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#666;line-height:1.5\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"width:360px;text-align:left;margin-top:50px;margin-bottom:80px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p>亲爱的"+"用户 " + userName +"，您好：</p>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-indent:2em\">\n" +
                        "请点击下面的链接修改用户密码：<br />"+
                        "<h1>忘了密码</h1><br/>"+
                        "<a href='http://10.21.48.11:8080/"+"anywork/html/newPassword.html?email=" +email+"&ciphertext="+ciphertext+"'>点击修改密码</a></p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"margin-bottom:40px;\"><a href=\"http://10.21.48.11:8080/anywork/html/login.html\" style=\"display:inline-block;width:139px;height:38px;line-height:38px;color:#fff;font-size:14px;vertical-align:middle;background:url(http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/0edb116f982044ba85ecd313f20e881c.jpg);text-decoration:none\">AnyWork</a></div>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:1px dashed #ccc;margin:20px\">&nbsp;</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"37px\" style=\"background:#dededc\">&nbsp;</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t</tbody>\n" +
                        "\t\t\t</table>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>\n" +
                        "</center>"
        ;
        return html;
    }

}
