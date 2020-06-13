package com.joo.dayo.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostData {
    public String explain; //글 내용
    public String photoName; //업로드 사진 이름
    public String uid; //누가 올렸는지(사용자 고유 uid)
    public String userId; //올린 유저 이메일
    public String timeStamp; //글 올린 시간
    public int favorite; //좋아요 개수
    public Map<String, Boolean> favorites = new HashMap<String,Boolean>(); //중복좋아요 방지(uid,좋아요여부)
    public int folderNum;

    //댓글
    public class Comment {
        String comUid;
        String comUserId;
        String comContent;
        Long comTimeStamp;
    }

    public PostData(String explain,String photoName, String uid, String userId, String timeStamp, int favorite, int folderNum) {
        this.explain = explain;
        this.photoName = photoName;
        this.uid = uid;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favorite = favorite;
        this.folderNum=folderNum;
        //Comment comment = new Comment();
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Map<String, Boolean> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public int getFolderNum() {
        return folderNum;
    }

    public void setFolderNum(int folderNum) {
        this.folderNum = folderNum;
    }

}
