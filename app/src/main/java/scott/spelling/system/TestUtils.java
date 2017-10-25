package scott.spelling.system;

import java.util.*;

import scott.spelling.model.*;

import static scott.spelling.model.Utils.*;

@SuppressWarnings("SameReturnValue")
public class TestUtils {
    public static SpellingList spellingListWeekOne() {return new SpellingList("Week One", "one", "two", "three");}
    public static SpellingList spellingListWeekTwo() {return new SpellingList("Week Two", "one", "two", "three");}
    public static SpellingLists spellingLists() { return new SpellingLists("", asList(spellingListWeekOne(), spellingListWeekTwo())); }
    public static SpellingLists emptySpellingLists() {return new SpellingLists("", new ArrayList<SpellingList>());}
}
