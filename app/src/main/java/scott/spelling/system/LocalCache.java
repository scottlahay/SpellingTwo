package scott.spelling.system;

import android.database.*;
import android.database.sqlite.*;

import java.util.*;

import scott.spelling.model.*;

public class LocalCache {
    public static final String SPELLING_LIST = "spelling_list";
    public static final String WORDS = "words";
    public static final String SYNC_KEY = "sync_key";

    private final SQLiteDatabase db;
    public LocalCache(SQLiteDatabase db) { this.db = db; }

    public boolean isEmpty() { return false; }
    public String key() { return null; }

    void createSpellingListTable() {db.execSQL("Create table  if not exists spelling_lists (name text not null );");}
    void createWordTable() {db.execSQL("Create table  if not exists words (id text not null, value text not null);");}
    void createSyncKeyTable() {db.execSQL("Create table  if not exists sync_key(id text not null);");}

    private void drop(String table) {
        try {
            db.execSQL("delete from " + table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveWordsToDb(String listId, String... wordList) {
        String sql = "Insert into words values ('%s', '%s');";
        for (String s : wordList) {
            db.execSQL(String.format(sql, listId, s));
        }
    }

    public void saveSpellingLists(String... ids) {
        String sql = "Insert into spelling_lists values ('%s');";
        for (String s : ids) {
            db.execSQL(String.format(sql, s));
        }
    }

    public void saveSyncKey(String syncKey) {
        String sql = "Insert into sync_key values ('%s');";
        db.execSQL(String.format(sql, syncKey));
    }

    public String getSyncKey() {
        return asObject(new SyncKeyMapper(), "select * from sync_key");
    }

    public SpellingLists getLists() {
        String sql = "select * from %s";
        String format = String.format(sql, "spelling_lists");
        return SpellingLists.create(asObjectList(new SpellingListMapper(), format));
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
        dropTables();
        createTables();
//        SpellingList spellingList = SpellingList.create("one_list", "one", "two", "three");
//        saveSpellingList(spellingList);
    }
    private void saveSpellingList(SpellingList spellingList) {
        saveSpellingLists(spellingList.id());
        saveWordsToDb(spellingList.id(), spellingList.words().toArray(new String[]{}));
    }

    void dropTables() {
//        drop(SYNC_KEY);
        drop(WORDS);
        drop(SPELLING_LIST);
    }
    void createTables() {
        createWordTable();
        createSpellingListTable();
//        createSyncKeyTable();
    }

    public void replaceAllLists(SpellingLists lists) {
        dropTables();
        createTables();
        for (SpellingList spellingList : lists.spellingLists().values()) {
            saveSpellingList(spellingList);
        }
    }

    public interface MyMapper<T> {
        T map(Cursor cursor);
    }

    public class WordMapper implements MyMapper<String> {
        public String map(Cursor cursor) {return cursor.getString(1);}
    }

    public class SyncKeyMapper implements MyMapper<String> {
        public String map(Cursor cursor) {return cursor.getString(0);}
    }

    public class SpellingListMapper implements MyMapper<SpellingList> {
        public SpellingList map(Cursor cursor) {return SpellingList.create(cursor.getString(0));}
    }
}
