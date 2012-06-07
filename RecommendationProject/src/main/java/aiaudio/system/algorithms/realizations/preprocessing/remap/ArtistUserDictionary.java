package aiaudio.system.algorithms.realizations.preprocessing.remap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Anastasiya
 */
public class ArtistUserDictionary {

    private Dictionary artists;
    private Dictionary users;

    public ArtistUserDictionary() {
        artists = new Dictionary();
        users = new Dictionary();
    }

    public int getAndAddIfNotPresentsArtist(int id) {
        return artists.getAndAddIfNotPresents(id);
    }

    public int getAndAddIfNotPresentsUsers(int id) {
        return users.getAndAddIfNotPresents(id);
    }

    public int artistsCount() {
        return artists.map.size();
    }

    public int usersCount() {
        return users.map.size();
    }

    public void write(String usersPath, String artistsPath) throws IOException {
        users.write(usersPath);
        artists.write(artistsPath);
    }

    private static class Dictionary {

        private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        private int current = -1;

        public int getAndAddIfNotPresents(int id) {
            if (map.containsKey(id)) {
                return map.get(id);
            }
            current++;
            map.put(id, current);
            return current;
        }

        private void write(String path) throws IOException {
            BufferedWriter w = new BufferedWriter(new FileWriter(path));
            w.write("#newId, oldId");
            w.newLine();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                String line = entry.getValue() + "\t" + entry.getKey();
                w.write(line);
                w.newLine();
            }
            w.flush();
            w.close();
        }
    }
}
