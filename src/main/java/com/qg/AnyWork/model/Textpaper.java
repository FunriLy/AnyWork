package com.qg.AnyWork.model;

import java.util.Date;
import java.util.List;

/**
 * 试卷实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Textpaper {

    private int textpaperId;                //试卷ID
    private String textpaperTitle;          //试卷标题
    private int authorId;                   //教师ID
    private Date createTime;                //开始时间
    private Date endingTime;                //结束时间
    private int chapter;                    //章节，为练习卷添加章节
    private int textpaperScore;             //试卷分数
    private int textpaperType;              //试卷类型，0为练习、1为考试，若将来扩展可在这个字段上实现

    public Textpaper(){}

    //get & set

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getTextpaperId() {
        return textpaperId;
    }

    public void setTextpaperId(int textpaperId) {
        this.textpaperId = textpaperId;
    }

    public String getTextpaperTitle() {
        return textpaperTitle;
    }

    public void setTextpaperTitle(String textpaperTitle) {
        this.textpaperTitle = textpaperTitle;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }

    public int getTextpaperScore() {
        return textpaperScore;
    }

    public void setTextpaperScore(int textpaperScore) {
        this.textpaperScore = textpaperScore;
    }

    public int getTextpaperType() {
        return textpaperType;
    }

    public void setTextpaperType(int textpaperType) {
        this.textpaperType = textpaperType;
    }

    //toString

    @Override
    public String toString() {
        return "Textpaper{" +
                "textpaperId=" + textpaperId +
                ", textpaperTitle='" + textpaperTitle + '\'' +
                ", authorId=" + authorId +
                ", createTime=" + createTime +
                ", endingTime=" + endingTime +
                ", chapter=" + chapter +
                ", textpaperScore=" + textpaperScore +
                ", textpaperType=" + textpaperType +
                '}';
    }
}
