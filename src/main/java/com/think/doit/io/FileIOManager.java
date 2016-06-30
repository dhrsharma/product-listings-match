package com.think.doit.io;

import com.think.doit.model.Match;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generic Abstract class providing common helper methods to read from/write to file.
 * @param <T>
 */
public abstract class FileIOManager<T extends Match> {

    public static final String spclCharRegex = "[^\\w\\s\\d]+";

    public static final String digitRegex = "[\\d]+";

    /**
     * This method will read the Json content of a file into its respective
     * Pojos and return a list of all such new line separated Json objects in
     * file.
     * 
     * @param filePath String value of file Path
     * @return {@link List} of Objects of Json lines from file.
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<T> readJsonIntoList(final String filePath) throws IOException, URISyntaxException {
        //Using java.io instead of NIO for now, due to resource path issue within Jar using Paths
        //Path path = Paths.get(getClass().getResource(filePath).toURI() );
        final List<T> listingsList = new ArrayList<>();
        //Files.newBufferedReader(path, StandardCharsets.UTF_8)
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)))) {
            dataEnricher(reader, listingsList);
            return listingsList;
        }
    }

    /**
     * Helper method to write output to the file.
     *
     * @param filePath
     * @param listToWrite
     * @throws IOException
     * @throws URISyntaxException
     */
    public void writeListOntoFile(final String filePath, final Collection<T> listToWrite) throws IOException, URISyntaxException {

        try (OutputStream matchedOut = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            dataWriter(matchedOut, listToWrite);
        } 
    }

    /**
     * This method provides data enrichment/manipulation logic to line items read from
     * file. Called by {@link #readJsonIntoList(String)}
     * 
     * @param reader {@link BufferedReader}
     * @param matchList list containing file line items
     */
    abstract void dataEnricher(final BufferedReader reader, final List<T> matchList);

    /**
     * This method provides logic to manipulate final output to be written on to the file.
     *
     * @param writer {@link java.io.OutputStream}
     * @param writeList collection containing final output
     */
    abstract void dataWriter(final OutputStream writer, final Collection<T> writeList);

}
