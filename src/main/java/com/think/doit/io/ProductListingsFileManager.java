package com.think.doit.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think.doit.model.ProductListings;

public class ProductListingsFileManager extends FileIOManager<ProductListings> {

    private final ObjectMapper mapper;

    public ProductListingsFileManager(final ObjectMapper objMapper) {
        this.mapper = objMapper;
    }

    @Override
    void dataWriter(final OutputStream writer, final Collection<ProductListings> writeCollection) {
        BlockingQueue<ProductListings> matchedQueue;
        ProductListings prdLstng;
        if (writeCollection instanceof BlockingQueue) {
            matchedQueue = (BlockingQueue<ProductListings>) writeCollection;
            try {
                while ((prdLstng = matchedQueue.poll(2000, TimeUnit.MILLISECONDS)) != null) {
                    writer.write(mapper.writeValueAsString(prdLstng).getBytes());
                    writer.write("\n".getBytes());
                }
            } catch (InterruptedException | IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }

        }
    }

    @Override
    void dataEnricher(BufferedReader reader, List<ProductListings> matchList) {
        // Do Nothing
    }

}
