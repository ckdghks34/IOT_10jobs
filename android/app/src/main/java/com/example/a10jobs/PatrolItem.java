package com.example.a10jobs;

import android.graphics.Bitmap;

public class PatrolItem {
    private Bitmap img; // 화면 캡쳐 사진
    private String date; // 시간 정보

//    public FindItem(String title, String date) {
//        this.title = title;
//        this.date = date;
//    }
    public PatrolItem(Bitmap img, String date) {
        this.img = img;
        this.date = date;
    }

    public Bitmap getImg() {
        return this.img;
    }
    public String getDate() {
        return this.date;
    }

//    public void setImg(Bitmap img) {
//        this.img = img;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }


}
