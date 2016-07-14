package com.gfcommunity.course.gfcommunity.model;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Product to present row in product list
 */
public class Product implements Serializable{
    private String productName;
    private int image;
    private String storeName;
    private String city;
    private String street;
    private int hoseNum;
    private String storeUrl;
    private String phone;
    private String comment;
    private String userID;
    private Timestamp createdAt;
    private String imgUri;

    public Product(){}

    public Product(String productName, int image, Timestamp createdAt, String userID, String comment, String phone, String storeName, String storeUrl, String imgUri) {
        this.productName = productName;
        this.image = image;
        this.createdAt = createdAt;
        this.userID = userID;
        this.comment = comment;
        this.phone = phone;
        this.storeName = storeName;
        this.storeUrl = storeUrl;
        this.imgUri = imgUri;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHoseNum() {
        return hoseNum;
    }

    public void setHoseNum(int hoseNum) {
        this.hoseNum = hoseNum;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setImgUrl(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgUrl() {
        return imgUri;

    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", image=" + image +
                ", storeName='" + storeName + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", hoseNum=" + hoseNum +
                ", storeUrl='" + storeUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", comment='" + comment + '\'' +
                ", userID='" + userID + '\'' +
                ", createdAt=" + createdAt +
                ", imgUri='" + imgUri + '\'' +
                '}';
    }
}
