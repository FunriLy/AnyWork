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

    public void send(String email, String userName){
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo(Sender);
            helper.setSubject("主题：HTML邮件");

            StringBuffer sb = new StringBuffer();
            helper.setText(htmlText(email, userName), true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MailSendException("发送忘了密码邮件失败");
        }
        javaMailSender.send(message);
    }


    private static String htmlText(String email, String userName) {
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
                        "\t\t\t\t\t\t\t\t\t<td height=\"70px\" valign=\"middle\" width=\"100%\"><img alt=\"logo\" src=\"http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/1686ccdd7919429a8beeb4f3f15d5eb1.png\" style=\"margin-left:50px;\" /></td>\n" +
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
                        "<h1>这是一个测试页面</h1><br/>"+
                        "<a href='http://127.0.0.1:8085/"+"forget.html?email=" +email+"&ciphertext="+ciphertext+"'>点击修改密码</a></p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"margin-bottom:40px;\"><a href=\"https://www.baidu.com\" style=\"display:inline-block;width:139px;height:38px;line-height:38px;color:#fff;font-size:14px;vertical-align:middle;background:url(http://7xi9bi.com1.z0.glb.clouddn.com/35069/2015/07/20/0edb116f982044ba85ecd313f20e881c.jpg);text-decoration:none\">修改密码</a></div>\n" +
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
