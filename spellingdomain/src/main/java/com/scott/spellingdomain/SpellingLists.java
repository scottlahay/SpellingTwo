package com.scott.spellingdomain;

import java.util.*;

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

    public String[] getAllListNames() {
        Set<String> strings = list.keySet();
        String[] results = strings.toArray(new String[strings.size()]);
        Arrays.sort(results, new Comparator<String>() {
            @Override public int compare(String one, String two) {
                if (one == null || two == null) { return 0; }
                return ((Integer) Integer.parseInt(one)).compareTo(Integer.parseInt(two));
            }
        });
        return results;
    }
}
