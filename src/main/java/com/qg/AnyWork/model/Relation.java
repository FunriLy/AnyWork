package com.qg.AnyWork.model;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Relation {

    private int relationId;     //ID
    private int organId;        //组织ID
    private int userId;         //用户ID
    private int role;           //角色，预留字段

    //get & set

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getOrganId() {
        return organId;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    //toString

    @Override
    public String toString() {
        return "Relation{" +
                "relationId=" + relationId +
                ", organId=" + organId +
                ", userId=" + userId +
                ", role=" + role +
                '}';
    }
}
