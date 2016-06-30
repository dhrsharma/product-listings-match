package com.think.doit.model;

import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

public class Listings implements Comparator<Listings>, Match {
    private String title;
    private String manufacturer;
    private String currency;
    private String price;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = StringUtils.isNotBlank(title) ? title.toLowerCase() : title;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = StringUtils.isNotBlank(manufacturer) ? manufacturer.toLowerCase() : manufacturer;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int compare(Listings o1, Listings o2) {
        return o1.getManufacturer().compareTo(o2.getManufacturer());
    }
    @Override
    public String toString() {
        return "Listings [title=" + title + ", manufacturer=" + manufacturer + ", currency=" + currency + ", price=" + price + "]";
    }

}
