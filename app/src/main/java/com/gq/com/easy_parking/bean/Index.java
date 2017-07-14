package com.gq.com.easy_parking.bean;

import java.util.List;

/**
 * Created by hasee on 2017/4/16.
 */
public class Index {
    private List<Image> images;
    private Product product;

    public Index() {
        super();
    }

    public Index(List<Image> images, Product product) {
        this.images = images;
        this.product = product;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
