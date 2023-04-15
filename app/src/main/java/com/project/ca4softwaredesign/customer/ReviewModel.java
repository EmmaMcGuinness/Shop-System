package com.project.ca4softwaredesign.customer;

public class ReviewModel {
    String userPhone;
    String productId;
    String review;
    String rating;

    public ReviewModel() {
    }

    public ReviewModel(String review, String rating, String userPhone, String productId) {
        this.review = review;
        this.rating = rating;
        this.userPhone = userPhone;
        this.productId = productId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
