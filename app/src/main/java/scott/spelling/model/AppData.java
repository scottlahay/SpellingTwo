package scott.spelling.model;

import com.google.auto.value.*;

import static scott.spelling.model.Utils.*;

@AutoValue
public abstract class AppData {

    public static AppData create(String currentTest, String syncKey) {
        return new AutoValue_AppData(currentTest, syncKey);
    }
    public abstract String currentTest();
    public abstract String syncKey();
    public boolean outdated() {
        boolean nullOrEmpty = isNullOrEmpty(currentTest());
        boolean nullOrEmpty1 = isNullOrEmpty(syncKey());
        return nullOrEmpty || nullOrEmpty1;
    }
}
