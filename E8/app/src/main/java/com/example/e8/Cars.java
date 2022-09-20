package com.example.e8;

public class Cars {

    private String imgName;
    private int imgNumber;

    public Cars(String imgName, int imgNumber) {
        this.imgName = imgName;
        this.imgNumber = imgNumber;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(int imgNumber) {
        this.imgNumber = imgNumber;
    }
}
