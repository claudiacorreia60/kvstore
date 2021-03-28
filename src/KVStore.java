import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KVStore {
    private Map<String, String> store;
    private File storeFile;
    private BufferedWriter fileWriter;

    public KVStore() {
        store = new HashMap<>();

        // Open log file
        String storeFilePath = System.getProperty("user.home") + "/.kvstore";
        storeFile = new File(storeFilePath);

        try {
            // Create file if it doesn't exist
            storeFile.createNewFile();
            // Recover persisted data
            recoverData();

            fileWriter = new BufferedWriter(new FileWriter(storeFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return store.get(key);
    }

    public void put(String key, String value) {
        store.put(key, value);
        persistEntry(key, value);
    }

    public void delete(String key) {
        store.remove(key);
        persistEntry("(-)", key);
    }

    private void recoverData() throws FileNotFoundException {
        Scanner storeReader = new Scanner(storeFile);

        // Go through all entries in the log
        while (storeReader.hasNextLine()) {
            String entry = storeReader.nextLine();
            recoverEntry(entry);
        }

        storeReader.close();
    }

    private void recoverEntry(String entry) {
        // Parse entry
        String key = entry.split(" ", 2)[0];
        String value = entry.split(" ", 2)[1];

        // Apply modification to the store
        if (key.equals("(-)"))
            store.remove(value);
        else store.put(key, value);
    }

    private void persistEntry(String key, String value) {
        String entry = key + " " + value;

        // Write entry to log
        try {
            fileWriter.write(entry);
            fileWriter.newLine();
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
