package com.qg.AnyWork.model;

import java.util.Date;
import java.util.List;

/**
 * 试卷实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Testpaper {

    private int testpaperId;                //试卷ID
    private String testpaperTitle;          //试卷标题
    private int authorId;                   //教师ID
    private int organizationId;             //组织ID        
    private Date createTime;                //开始时间
    private Date endingTime;                //结束时间
    private int chapterId;                    //章节id，为练习卷添加章节
    private String chapterName;               //章节名称，为练习卷添加章节
    private int testpaperScore;             //试卷分数
    private int testpaperType;              //试卷类型，0为练习、1为考试，若将来扩展可在这个字段上实现

    public Testpaper(){}

    //get & set


    @Override
    public String toString() {
        return "Testpaper{" +
                "testpaperId=" + testpaperId +
                ", testpaperTitle='" + testpaperTitle + '\'' +
                ", authorId=" + authorId +
                ", organizationId=" + organizationId +
                ", createTime=" + createTime +
                ", endingTime=" + endingTime +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", testpaperScore=" + testpaperScore +
                ", testpaperType=" + testpaperType +
                '}';
    }

    public int getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(int testpaperId) {
        this.testpaperId = testpaperId;
    }

    public String getTestpaperTitle() {
        return testpaperTitle;
    }

    public void setTestpaperTitle(String testpaperTitle) {
        this.testpaperTitle = testpaperTitle;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
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

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getTestpaperScore() {
        return testpaperScore;
    }

    public void setTestpaperScore(int testpaperScore) {
        this.testpaperScore = testpaperScore;
    }

    public int getTestpaperType() {
        return testpaperType;
    }

    public void setTestpaperType(int testpaperType) {
        this.testpaperType = testpaperType;
    }
}
