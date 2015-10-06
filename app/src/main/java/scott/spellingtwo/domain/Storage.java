package scott.spellingtwo.domain;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Storage {

    SharedPreferences preferences;

    public Storage(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public void save(String key, Set<String> value) {
        preferences.edit().putStringSet(key, value).commit();
    }

    public Set<String> retrieve(String key) {
        Set<String> set = new HashSet<>();
        preferences.getStringSet(key, set);
        return set;
    }

}
