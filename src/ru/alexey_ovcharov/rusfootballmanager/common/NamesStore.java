package ru.alexey_ovcharov.rusfootballmanager.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey
 */
public class NamesStore {

    private static final NamesStore INSTANCE
            = new NamesStore();
    private final List<String> firstNames = new ArrayList<>();
    private final List<String> lastNames = new ArrayList<>();

    private NamesStore() {
        File firstNamesFile = new File("fnames");
        File lastNamesFile = new File("lnames");
        fillCollection(firstNames, firstNamesFile);
        fillCollection(lastNames, lastNamesFile);
    }

    private void fillCollection(List<String> collection, File file) {
        try (BufferedReader firstNamesReader
                = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            while (firstNamesReader.ready()) {
                String line = firstNamesReader.readLine();
                collection.add(line);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static NamesStore getInstance() {
        return INSTANCE;
    }

    public String getRandomFirstName() {
        return firstNames.get(Randomization.RANDOM.nextInt(firstNames.size()));
    }

    public String getRandomLastName() {
        return lastNames.get(Randomization.RANDOM.nextInt(lastNames.size()));
    }
}
