package com.think.doit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think.doit.io.FileIOManager;
import com.think.doit.io.ListingsFileManager;
import com.think.doit.io.ProductListingsFileManager;
import com.think.doit.io.ProductsFileManager;
import com.think.doit.model.Listings;
import com.think.doit.model.ProductListings;
import com.think.doit.model.Products;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ProductListingsMatch {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private static final String listingsPath = "/listings.txt";
    private static final String productsPath = "/products.txt";
    private static final String matchedListings = "./matched_listings.txt";
    private static final Map<String, List<Listings>> manfcListingsMap = new HashMap<>();
    private static final BlockingQueue<ProductListings> matchedQueue = new LinkedBlockingQueue<>(1000);

    public static void main(String[] args) {
        try {
            FileIOManager<Listings> listingsFileManager = new ListingsFileManager(new ObjectMapper());
            FileIOManager<Products> productsFileManager = new ProductsFileManager(new ObjectMapper());
            FileIOManager<ProductListings> productListingsFileManager = new ProductListingsFileManager(new ObjectMapper());

            List<Listings> listingsList = listingsFileManager.readJsonIntoList(listingsPath);
            List<Products> productList = productsFileManager.readJsonIntoList(productsPath);

            String manufacturer = "";
            List<Listings> tempListings = null;
            for (Listings obj : listingsList) {
                if (!obj.getManufacturer().equals(manufacturer)) {
                    if (StringUtils.isNotBlank(manufacturer) && tempListings != null) {
                        manfcListingsMap.put(manufacturer, tempListings);
                    }
                    tempListings = new ArrayList<>();
                    manufacturer = obj.getManufacturer();
                }
                tempListings.add(obj);
            }

            System.out.println("After optimized Listings map creation.");

            for (Products obj : productList) {
                final Products forExecProd = obj;
                executorService.execute(new Runnable() {
                    public void run() {
                        try {
                            doMatch(forExecProd);
                        } catch (InterruptedException ie) {
                            System.out.println("Record failed: " + forExecProd.toString());
                        }
                    }
                });
            }

            productListingsFileManager.writeListOntoFile(matchedListings, matchedQueue);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void doMatch(final Products product) throws InterruptedException {
        final String rgx = "[\\W\\D\\s]+";
        List<Listings> listFromMap = manfcListingsMap.get(product.getManufacturer());
        ProductListings prodListings = null;
        String title;
        if (listFromMap != null && listFromMap.size() > 0) {
            for (Listings listing : listFromMap) {
                title = listing.getTitle();
                if (title.contains(product.getFamily()) && title.contains(product.getManufacturer()) && title.contains(product.getModel())
                        && title.matches(rgx + product.getModel() + rgx)) {
                    if (prodListings == null) {
                        prodListings = new ProductListings();
                        prodListings.setProductname(product.getProductName());
                    }
                    prodListings.getListings().add(listing);
                }
            }
        }
        if (prodListings != null) {
            matchedQueue.offer(prodListings, 1000, TimeUnit.MILLISECONDS);
        }
    }

}
