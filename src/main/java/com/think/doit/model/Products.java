package com.think.doit.model;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Products implements Comparator<Products>, Match {
    @JsonProperty("product_name")
    private String productName;
    private String manufacturer;
    private String model;
    private String family;
    @JsonProperty("announced-date")
    private String announcedDate;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = StringUtils.isNotBlank(productName) ? productName.toLowerCase() : "";
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = StringUtils.isNotBlank(manufacturer) ? manufacturer.toLowerCase() : "";
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = StringUtils.isNotBlank(model) ? model.toLowerCase() : "";
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = StringUtils.isNotBlank(family) ? family.toLowerCase() : "";
    }

    public String getAnnouncedDate() {
        return announcedDate;
    }

    public void setAnnouncedDate(String announcedDate) {
        this.announcedDate = announcedDate;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productName='" + productName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", family='" + family + '\'' +
                ", announcedDate='" + announcedDate + '\'' +
                '}';
    }

    @Override
    public int compare(Products o1, Products o2) {
        return o1.getManufacturer().compareTo(o2.getManufacturer());
    }

}
