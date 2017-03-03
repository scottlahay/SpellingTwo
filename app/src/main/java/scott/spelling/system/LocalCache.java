package scott.spelling.system;

import android.database.*;
import android.database.sqlite.*;

import java.util.*;

import scott.spelling.model.*;

public class LocalCache {
    private final SQLiteDatabase db;
    public LocalCache(SQLiteDatabase db) { this.db = db; }

    public boolean isEmpty() { return false; }
    public String key() { return null; }

    void createSpellingListTable() {db.execSQL("Create table  if not exists spelling_lists (name text not null );");}
    void createWordTable() {db.execSQL("Create table  if not exists words (id text not null, value text not null);");}

    void saveWordsToDb(String listId, String... wordlist) {
        String sql = "Insert into words values ('%s', '%s');";
        for (String s : wordlist) {
            db.execSQL(String.format(sql, listId, s));
        }
    }

    public void saveSpellingLists(String... ids) {
        String sql = "Insert into spelling_lists values ('%s');";
        for (String s : ids) {
            db.execSQL(String.format(sql, s));
        }
    }

    public SpellingLists getLists() {
        String sql = "select * from %s";
        String format = String.format(sql, "spelling_lists");
        SpellingLists all = SpellingLists.create(asObjectList(new SpellingListMapper(), format));
        return all;
    }

    public SpellingList getList(String key) {
        String sql = "select * from %s where name = '%s'";
        String format = String.format(sql, "spelling_lists", key);
        SpellingList spellingList = asObject(new SpellingListMapper(), format);
        spellingList.replace(getWords(key));
        return spellingList;
    }

    List<String> getWords(String key) {
        String sql = "select * from %s where id = '%s'";
        String format = String.format(sql, "words", key);
        return asObjectList(new WordMapper(), format);
    }

    private <T> List<T> asObjectList(MyMapper<T> mapper, String sql) {
        List<T> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            list.add(mapper.map(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private <T> T asObject(MyMapper<T> mapper, String sql) {
        return asObjectList(mapper, sql).get(0);
    }
    public void init() {
        createWordTable();
        createSpellingListTable();
        String[] wordlist = new String[]{"one", "two", "three"};
        saveWordsToDb("list_one", wordlist);
        saveSpellingLists("list_one");
    }

    public interface MyMapper<T> {
        T map(Cursor cursor);
    }

    public class WordMapper implements MyMapper<String> {
        public String map(Cursor cursor) {return cursor.getString(1);}
    }

    public class SpellingListMapper implements MyMapper<SpellingList> {
        public SpellingList map(Cursor cursor) {return SpellingList.create(cursor.getString(0));}
    }
}
