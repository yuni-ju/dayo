package com.joo.dayo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PostData {
    String explain;
    String[] uris = new String[5];
    String uid; //누가 올렸는지
    String userId; //올린 유저 관리
    Long timeStamp;
    int favorite; //좋아요 개수
    Map<String, Boolean> favorites = new HashMap<String,Boolean>(); //중복좋아요 방지

    public class Comment{ //댓글 관리
        String uid;
        String userId;
        String comment;
        Long timeStamp;

        public Comment(String uid, String userId, String comment, Long timeStamp) {
            this.uid = uid;
            this.userId = userId;
            this.comment = comment;
            this.timeStamp = timeStamp;
        }
    }
}
