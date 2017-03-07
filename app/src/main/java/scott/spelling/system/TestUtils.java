package scott.spelling.system;

import java.util.*;

import scott.spelling.model.*;

import static scott.spelling.model.Utils.*;

public class TestUtils {
    public static String currentKey() { return "validKey"; }
    public static String oldKey() { return "oldKey"; }
    public static SpellingList spellingList() {return SpellingList.create("Test Spelling one", "one", "two", "three");}
    public static SpellingLists spellingLists() { return SpellingLists.create(asList(spellingList())); }
    public static SpellingLists emptySpellingLists() {return SpellingLists.create(new ArrayList<SpellingList>());}
}
