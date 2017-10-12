package com.qg.AnyWork.utils;

import com.qg.AnyWork.model.Message;
import com.qg.AnyWork.model.Testpaper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FunriLy on 2017/10/4.
 * From small beginnings comes great things.
 */
public class MessageUtil {

    /**
     * 教师发布试卷、练习时发布通知
     * @param useridList
     * @param testpaper
     * @return
     */
    public static List<Message> getTestPaperMessageList(List<Integer> useridList, Testpaper testpaper){
        List<Message> messageList = new ArrayList<>();
        int authorId = testpaper.getAuthorId();
        String typeName = testpaper.getTestpaperType() == 0 ? "练习" : "试卷";
        String content = authorId + "发布了一套" + typeName
                + ":《" + testpaper.getTestpaperTitle() + "》，结束时间为："
                + DateUtil.dateToString(testpaper.getEndingTime()) + ", 请尽快完成！";
        for (int userid : useridList) {
            Message message = new Message(authorId, userid, content, 1);
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 教师阅卷后给予评判分数
     * @return
     */
    public static Message getScoreMessage(){
        return new Message();
    }
}
