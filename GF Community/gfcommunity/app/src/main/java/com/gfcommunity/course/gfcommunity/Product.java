package com.gfcommunity.course.gfcommunity;

import java.util.Date;

/**
 * Product to present row in product list
 */
public class Product {
    private String productName;
    private String storeName;
    private Date dateModified;
    private String userID;
    private int image;

    public Product(){}

    public Product(String productName, String storeName, Date dateModified, String userID, int image) {
        this.productName = productName;
        this.storeName = storeName;
        this.dateModified = dateModified;
        this.userID = userID;
        this.image = image;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getUserID() {
        return userID;
}

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", dateModified=" + dateModified +
                ", userID='" + userID + '\'' +
                '}';
    }
}
