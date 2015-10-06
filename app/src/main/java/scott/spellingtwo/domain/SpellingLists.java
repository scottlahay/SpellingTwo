package scott.spellingtwo.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SpellingLists {


    private final HashMap<String, SpellingList> list = new HashMap<>();

    public SpellingLists(List<String> csvList) {
        for (String string : csvList) {
            String[] temp = string.split(",");
            String[] words = Arrays.copyOfRange(temp, 1, temp.length);
            list.put(temp[0], new SpellingList(words));
        }
    }

    public SpellingList findList(String name) {
        return list.get(name);
    }

    public static SpellingLists load() {
        return new SpellingLists(new FileDownloader().download(UrlUtil.KIDS_SPELLING));
    }

    public String[] getAllListNames() {
        Set<String> strings = list.keySet();
        String[] results = strings.toArray(new String[strings.size()]);
        Arrays.sort(results);
        return results;
    }
}
