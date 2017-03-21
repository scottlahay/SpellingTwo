package scott.spelling.system;

import android.database.*;
import android.database.sqlite.*;
import android.util.*;

import java.util.*;

import scott.spelling.model.*;

import static scott.spelling.view.SpellingActivity.*;

public class LocalCache {
    public static final String SPELLING_LIST = "spelling_list";
    public static final String WORDS = "words";
    public static final String APP_DATA = "app_data";

    private final SQLiteDatabase db;
    public LocalCache(SQLiteDatabase db) { this.db = db; }

    public boolean isEmpty() { return false; }
    public String key() { return null; }

    void createSpellingListTable() {db.execSQL(String.format("Create table  if not exists %S (name text not null );", SPELLING_LIST));}
    void createWordTable() {db.execSQL(String.format("Create table  if not exists %s(id text not null, value text not null);", WORDS));}
    void createAppDataTable() {db.execSQL(String.format("Create table  if not exists %s(current text not null, syncKey text not null );", APP_DATA));}

    private void drop(String table) {
        try {
            db.execSQL("drop table if exists " + table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveWordsToDb(String listId, String... wordList) {
        String sql = "Insert into %s values ('%s', '%s');";
        for (String s : wordList) {
            db.execSQL(String.format(sql, WORDS, listId, s));
        }
    }

    public void saveSpellingLists(String... ids) {
        String sql = "Insert into %s values ('%s');";
        for (String s : ids) {
            db.execSQL(String.format(sql, SPELLING_LIST, s));
        }
    }

    public void saveAppData(AppData appData) {
        db.delete(APP_DATA, null, null);
        db.execSQL(String.format("Insert into %s values ('%s','%s');", APP_DATA, appData.currentTest(), appData.syncKey()));
    }

    public AppData getAppData() {
        Log.d(TAG_IT, "getAppData: LocalCache");
        return asObject(new AppDataMapper(), String.format("select * from %s", APP_DATA));
    }

    public SpellingLists getLists() {
        return SpellingLists.create(asObjectList(new SpellingListMapper(), String.format("select * from %s", SPELLING_LIST)));
    }

    public SpellingList getList(String key) {
        SpellingList spellingList = asObject(new SpellingListMapper(), String.format("select * from %s where name = '%s'", SPELLING_LIST, key));
        spellingList.replace(getWords(key));
        return spellingList;
    }

    List<String> getWords(String key) {
        return asObjectList(new StringMapper(1), String.format("select * from %s where id = '%s'", WORDS, key));
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

    private <T> T asObject(MyMapper<T> mapper, String sql) { return asObjectList(mapper, sql).get(0); }

    public void init() {
        dropTables();
        createTables();
    }

    private void saveSpellingList(SpellingList spellingList) {
        saveSpellingLists(spellingList.id());
        saveWordsToDb(spellingList.id(), spellingList.words().toArray(new String[]{}));
    }

    void dropTables() {
        drop(APP_DATA);
        drop(WORDS);
        drop(SPELLING_LIST);
    }
    LocalCache createTables() {
        createWordTable();
        createSpellingListTable();
        createAppDataTable();
        return this;
    }

    public void replaceAllLists(SpellingLists lists) {
        dropTables();
        createTables();
        for (SpellingList spellingList : lists.spellingLists().values()) {
            saveSpellingList(spellingList);
        }
    }
    public Void initCheck() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + APP_DATA + "'", null);
        if (cursor != null && cursor.getCount() == 0) {
            init();
        }
        cursor.close();
        Log.d(TAG_IT, "initCheck: initcheck");
        return null;
    }

    public interface MyMapper<T> {
        T map(Cursor cursor);
    }

    public class StringMapper implements MyMapper<String> {
        int index;
        public StringMapper(int index) { this.index = index; }
        public StringMapper() { this(0); }

        public String map(Cursor cursor) {return cursor.getString(index);}
    }

    public class SpellingListMapper implements MyMapper<SpellingList> {
        public SpellingList map(Cursor cursor) {return SpellingList.create(cursor.getString(0));}
    }

    public class AppDataMapper implements MyMapper<AppData> {
        @Override public AppData map(Cursor cursor) {
            return AppData.create(cursor.getString(0), cursor.getString(1));
        }
    }
}
