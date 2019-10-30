package com.grouptwo.ustcdiary.topicActivity;

/**
 * 备忘录实体类
 * craeted by mingjian on 2019/10/23
 */
public class MemoEntity {
    private int memoId;
    private String content;
    private int isDeleted;
    private int orderNum;

    public MemoEntity(int memoId, String content, int isDeleted,int orderNum) {
        this.memoId = memoId;
        this.content = content;
        this.isDeleted = isDeleted;
        this.orderNum=orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public long getMemoId() {
        return memoId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int i) {
        isDeleted = i;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
