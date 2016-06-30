package com.think.doit.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think.doit.model.Listings;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ListingsFileManager extends FileIOManager<Listings> {

    private final ObjectMapper mapper;

    public ListingsFileManager(final ObjectMapper objMapper) {
        this.mapper = objMapper;
    }

    @Override
    void dataEnricher(final BufferedReader reader, final List<Listings> listingsList) {
        final Set<String> manufacturerSet = new HashSet<>();
        final List<String> bufferList = new ArrayList<>();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                Listings oneListing = mapper.readValue(line, Listings.class);
                if (StringUtils.isBlank(oneListing.getManufacturer()) || oneListing.getManufacturer().matches(FileIOManager.digitRegex)
                                || oneListing.getManufacturer().matches(FileIOManager.spclCharRegex)) {
                    bufferList.add(line);
                    continue;
                }
                manufacturerSet.add(oneListing.getManufacturer());
                listingsList.add(oneListing);
            }
            for (String lineStr : bufferList) {
                Listings listing = mapper.readValue(lineStr, Listings.class);
                String title = listing.getTitle();
                for (String manfctStr : manufacturerSet) {
                    if (StringUtils.isBlank(title) && (title.contains(manfctStr))) {
                        listing.setManufacturer(manfctStr);
                        break;
                    } else if (listing.getManufacturer().matches(FileIOManager.digitRegex) || listing.getManufacturer().matches(FileIOManager.spclCharRegex)) {
                        listing.setManufacturer(title.split(" ")[0]);
                        break;
                    }
                }
            }
            Collections.sort(listingsList, new Listings());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    void dataWriter(OutputStream writer, Collection<Listings> writeList) {
        // Do Nothing        
    }
}