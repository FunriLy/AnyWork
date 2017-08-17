package com.qg.AnyWork.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by FunriLy on 2017/8/18.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/utils")
public class UtilController {

    /**
     * 生成图片验证码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/valcode",  produces="application/json;charset=UTF-8")
    @ResponseBody
    public String Verification(HttpServletRequest request, HttpServletResponse response){

        // 告知浏览当作图片处理
        response.setContentType("image/jpeg");
        // 告诉浏览器不缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        // 产生由4位数字构成的验证码
        int length = 4;
        String valcode  = "";
        Random rd =  new Random();
        for(int i=0; i<length; i++)
            valcode+=rd.nextInt(10);
        // 把产生的验证码存入到Session中
        HttpSession session = request.getSession();
        session.setAttribute("valcode", valcode);

        // 产生图片
        int width = 120;
        int height = 30;
        BufferedImage img = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        // 获取一个Graphics
        Graphics g = img.getGraphics();
        // 填充背景色
        g.setColor(Color.WHITE);
        //填充指定的矩形
        g.fillRect(0, 0, width, height);
        // 填充干扰线50
        for(int i=0; i<50; i++){
            g.setColor(new Color(rd.nextInt(100)+155,rd.nextInt(100)+155,rd.nextInt(100)+155));
            g.drawLine(rd.nextInt(width), rd.nextInt(height),rd.nextInt(width), rd.nextInt(height));
        }
        // 绘制边框
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width-1, height-1);//边框
        // 绘制验证码
        for(int i=0; i<length; i++){
            g.setColor(new Color(rd.nextInt(150),rd.nextInt(150),rd.nextInt(150)));
            g.drawString(valcode.charAt(i)+"", width/valcode.length()*i+2, 18);
        }
        // 输出图像
        g.dispose();
        try {
            ImageIO.write(img, "jpeg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}
