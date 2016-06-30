package com.think.doit.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"product_name", "listings"})
public class ProductListings implements Match{
    
    @JsonProperty("product_name")
    private String productname;
    
    private List<Listings> listings = new ArrayList<>();

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public List<Listings> getListings() {
        return listings;
    }

}
