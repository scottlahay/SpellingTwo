package scott.spellingtwo.domain;

import java.util.ArrayList;

public class SpellingList {

    public ArrayList<String> items = new ArrayList<>();
    public int current = 0;

    public SpellingList(String... items) {
        for (String item : items) {
            add(item);
        }
    }

    public SpellingList(SpellingList... lists) {
        for (SpellingList list : lists) {
            for (String word : list.items) {
                add(word);
            }
        }
    }

    public void add(String word) {
        items.add(word.trim());
    }

    public int size() {
        return items.size();
    }

    public String currentWord() {
        return items.get(current);
    }

    public void nextWord() {
        if (finished()) {
            reset();
        } else {
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
        return items.contains(word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpellingList that = (SpellingList) o;

        if (current != that.current) return false;
        boolean equals = items.equals(that.items);
        return !(items != null ? !equals : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + current;
        return result;
    }

    public boolean isCorrectAnswer(String answer) {
        return currentWord().equals(answer);
    }
}
