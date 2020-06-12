package com.joo.dayo;

import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostData {
    public String explain; //글 내용
    public List<String> photoName; //업로드 사진 이름
    public String uid; //누가 올렸는지(사용자 고유 uid)
    public String userId; //올린 유저 이메일
    public String timeStamp; //글 올린 시간
    public int favorite; //좋아요 개수
    public Map<String, Boolean> favorites = new HashMap<String,Boolean>(); //중복좋아요 방지(uid,좋아요여부)
    //댓글
    public class Comment {
        String comUid;
        String comUserId;
        String comContent;
        Long comTimeStamp;
    }

    public PostData(String explain,List<String> photoName, String uid, String userId, String timeStamp) {
        this.explain = explain;
        this.photoName = photoName;
        this.uid = uid;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.favorite=0;
        //Comment comment = new Comment();
    }




}
