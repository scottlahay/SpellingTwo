package scott.spelling.system;

import android.content.*;
import android.database.sqlite.*;

public class OpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "spelling.db";
    public static final int DB_VERSION = 1;

    public static OpenHelper instance;

    public static OpenHelper instance(Context context) {
        if (instance == null) { instance = new OpenHelper(context); }
        return instance;
    }

    private OpenHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }// not necessary till I get to Version Deux
}
