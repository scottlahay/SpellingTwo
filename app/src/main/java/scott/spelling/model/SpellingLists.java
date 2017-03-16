package scott.spelling.model;

import com.google.auto.value.*;

import java.util.*;

import static scott.spelling.model.Utils.*;

@AutoValue
public abstract class SpellingLists {

    public static SpellingLists createCsv(List<String> csvList) {
        List<SpellingList> temp = new ArrayList<>();
        for (String string : csvList) {
            String[] id = string.split(",");
            String[] words = Arrays.copyOfRange(id, 1, id.length);
            temp.add(SpellingList.create(id[0], Arrays.asList(words)));
        }
        return create(temp);
    }
    public static SpellingLists create(List<SpellingList> lists) {

        HashMap<String, SpellingList> temp = new HashMap<>();
        for (SpellingList spellingList : lists) {
            temp.put(spellingList.id(), spellingList);
        }
        return new AutoValue_SpellingLists(temp);
    }
    public abstract HashMap<String, SpellingList> spellingLists();
    public String[] listNames() {
        Set<String> strings = spellingLists().keySet();
        String[] strings1 = strings.toArray(new String[strings.size()]);
        Arrays.sort(strings1);
        return strings1;
//        Arrays.sort(results, new Comparator<String>() {
//            @Override public int compare(String one, String two) {
//                if (one == null || two == null) { return 0; }
//                return ((Integer) Integer.parseInt(one)).compareTo(Integer.parseInt(two));
//            }
//        });
//        return results;
    }

    public boolean empty() { return isNullOrEmpty(spellingLists()); }
    public SpellingList findList(String name) { return spellingLists().get(name); }
    public SpellingList findList(int index) { return spellingLists().get(listNames()[index]); }
}
