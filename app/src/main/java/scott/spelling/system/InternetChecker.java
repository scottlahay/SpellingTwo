package scott.spelling.system;

import scott.spelling.model.*;

public class InternetChecker {
    public  String key;

    public boolean available() {
        return false;
    }
    public boolean listIsOld(LocalCache localCache) {
        if (localCache.isEmpty()) { return true; }
        else { return !getKey().equals(localCache.key()); }
    }

    private String getKey() {return key;} // yes this will have to connect to google sheets

    public SpellingList getLists() {
        return null;
    }
}
