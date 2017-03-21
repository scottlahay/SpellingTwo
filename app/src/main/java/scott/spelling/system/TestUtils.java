package scott.spelling.system;

import java.util.*;

import scott.spelling.model.*;

import static scott.spelling.model.Utils.*;

@SuppressWarnings("SameReturnValue")
public class TestUtils {
    public static String currentKey() { return "validKey"; }
    public static String oldKey() { return "oldKey"; }
    public static SpellingList spellingListWeekOne() {return SpellingList.create("Week One", "one", "two", "three");}
    public static SpellingList spellingListWeekTwo() {return SpellingList.create("Week Two", "one", "two", "three");}
    public static SpellingLists spellingLists() { return SpellingLists.create(asList(spellingListWeekOne(), spellingListWeekTwo())); }
    public static SpellingLists emptySpellingLists() {return SpellingLists.create(new ArrayList<SpellingList>());}
    public static AppData appData() {return AppData.create("Week One", "123");}
    public static AppData appData2() {return AppData.create("Week Two", "345");}
}
