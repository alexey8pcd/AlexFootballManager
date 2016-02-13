package rusfootballmanager.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        File firstNamesFile = new File("first_names");
        File lastNamesFile = new File("last_names");
        fillCollection(firstNames, firstNamesFile);
        fillCollection(lastNames, lastNamesFile);
    }

    private void fillCollection(List<String> collection, File firstNamesFile) {
        try {
            try (BufferedReader firstNamesReader
                    = new BufferedReader(new FileReader(firstNamesFile))) {
                while (firstNamesReader.ready()) {
                    collection.add(firstNamesReader.readLine());
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static NamesStore getInstance() {
        return INSTANCE;
    }

    public String getRandomFirstName() {
        return firstNames.get(Constants.RANDOM.nextInt(firstNames.size()));
    }

    public String getRandomLastName() {
        return lastNames.get(Constants.RANDOM.nextInt(lastNames.size()));
    }
}
