package com.joo.dayo.data;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    String status;
    List<String> folderList;
    int folderNum;

    public UserData(){}

    public UserData(String status, List<String> folderList) {
        this.status = status;
        this.folderList = folderList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFolder() {
        return folderList;
    }

    public int getFolderNum() {
        folderNum = folderList.size();
        return folderNum;
    }

    public void setFolder(List<String> folderList) {
        this.folderList = folderList;
    }
}
