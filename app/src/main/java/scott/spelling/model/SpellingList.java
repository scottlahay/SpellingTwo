package scott.spelling.model;

import com.google.auto.value.*;

import java.util.*;

@AutoValue
public abstract class SpellingList {

    public int current;
    public static SpellingList create(String id, String... words) { return create(id, Utils.asList(words)); }
    public static SpellingList create(String id) { return create(id, new ArrayList<String>()); }
    public static SpellingList create(String id, List<String> words) { return new AutoValue_SpellingList(id, words); }
    public abstract String id();
    public abstract List<String> words();
    public void nextWord() {
        if (atEnd()) { setAtStart(); }
        else { current++; }
    }

    public void replace(List<String> words) {
        words().clear();
        words().addAll(words);
    }

    private int lastIndex() {return size() - 1;}
    public void setAtEnd() { current = lastIndex(); }
    public boolean atEnd() { return current == lastIndex(); }
    public void setAtStart() { current = 0; }
    public boolean atStart() { return current == 0; }
    public boolean contains(String word) { return words().contains(word); }
    public boolean isCorrectAnswer(String answer) { return currentWord().equals(answer); }
    public void add(String word) { words().add(word.trim()); }

    public int size() { return words().size(); }
    public String currentWord() { return words().get(current); }
    public int primaryProgress() { return calcProgress(current); }
    public int secondaryProgress() { return calcProgress(current + 1); }
    private int calcProgress(int index) {return (int) ((double) index / (double) size() * 100);}
}
