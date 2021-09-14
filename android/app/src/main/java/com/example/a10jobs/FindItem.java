package com.example.a10jobs;

public class FindItem {
    private int img; // 화면 캡쳐 사진
    private String title; // 찾을 물건 정보
    private String date; // 시간 정보

//    public FindItem(String title, String date) {
//        this.title = title;
//        this.date = date;
//    }

    public int getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FindItem(int img, String title, String date) {
        this.img = img;
        this.title = title;
        this.date = date;
    }
}