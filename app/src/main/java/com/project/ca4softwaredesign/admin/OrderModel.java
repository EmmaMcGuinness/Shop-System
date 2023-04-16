package com.project.ca4softwaredesign.admin;

import java.io.Serializable;

public class OrderModel implements Serializable {
    String orderId;
    String userId;
    String total;
    String productName;
    String quantity;

    public OrderModel() {
    }

    public OrderModel(String orderId, String userId, String total) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
    }
    public OrderModel(String productName, String quantity){
        this.productName = productName;
        this.quantity = quantity;

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
