package scott.spelling.model;

import com.google.auto.value.*;

import java.util.*;

@AutoValue
public abstract class SpellingList {

    public int current;
    public abstract String id();
    public abstract List<String> words();

    public static SpellingList create(String id) { return create(id, new ArrayList<String>()); }
    public static SpellingList create(String id,String... words) { return create(id, Arrays.asList(words)); }
    public static SpellingList create(String id, List<String> words) { return new AutoValue_SpellingList(id, words); }

//    public SpellingList(String... words) {
//        for (String item : words) {
//            add(item);
//        }
//    }
//
//    public SpellingList(SpellingList... lists) {
//        for (SpellingList list : lists) {
//            for (String word : list.words()) {
//                add(word);
//            }
//        }
//    }

    public void add(String word) {
        words().add(word.trim());
    }

    public int size() {
        return words().size();
    }

    public String currentWord() {
        return words().get(current);
    }

    public void nextWord() {
        if (finished()) {
            reset();
        }
        else {
            current++;
        }
    }

    public boolean finished() {
        return current == size() - 1;
    }

    public void reset() {
        current = 0;
    }

    public boolean contains(String word) {
        return words().contains(word);
    }

    public boolean isCorrectAnswer(String answer) {
        return currentWord().equals(answer);
    }
    public void replace(List<String> words) {
        words().clear();
        words().addAll(words);
    }
}
