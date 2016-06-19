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
    private String address;
    private String storeUrl;
    private String phone;
    private String comment;
    private String userID;
    private Timestamp createdAt;

    public Product(){}

    public Product(String productName, int image, Timestamp createdAt, String userID, String comment, String phone, String address, String storeName, String storeUrl) {
        this.productName = productName;
        this.image = image;
        this.createdAt = createdAt;
        this.userID = userID;
        this.comment = comment;
        this.phone = phone;
        this.address = address;
        this.storeName = storeName;
        this.storeUrl = storeUrl;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
                ", address='" + address + '\'' +
                ", storeUrl='" + storeUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", comment='" + comment + '\'' +
                ", userID='" + userID + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
