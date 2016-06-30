package com.think.doit.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think.doit.model.Products;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductsFileManager extends FileIOManager<Products> {

    private final ObjectMapper mapper;

    public ProductsFileManager(final ObjectMapper objMapper) {
        this.mapper = objMapper;
    }
    
    @Override
    void dataEnricher(BufferedReader reader, List<Products> productList) {
        String line;
        Products product;
        try {
            while ((line = reader.readLine()) != null) {
                product = mapper.readValue(line, Products.class);
                if (StringUtils.isBlank(product.getManufacturer()) || StringUtils.isBlank(product.getModel()) || StringUtils.isBlank(product.getFamily())) {
                    if (StringUtils.isBlank(product.getFamily())) {
                        product.setFamily("");
                    } else if (StringUtils.isBlank(product.getManufacturer())) {
                        product.setManufacturer("");
                    } else if (StringUtils.isBlank(product.getModel())) {
                        product.setModel("");
                    }
                }
                productList.add(product);
            }
            Collections.sort(productList, new Products());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    void dataWriter(OutputStream writer, Collection<Products> writeList) {
        // Do Nothing        
    }

}
