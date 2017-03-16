package scott.spelling.system;

import android.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static android.content.ContentValues.*;

public class FileDownloader {

    public List<String> download(String urlToDownload) {
        try {
            URL url = new URL(UrlUtil.buildUrl(urlToDownload));
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader file = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            List<String> lines = new ArrayList<>();
            String line = file.readLine();
            while (line != null) {
                lines.add(line);
                line = file.readLine();
            }
            return lines;
        } catch (Exception e) {
            Log.d(TAG, "download: ");
        }
        return null;
    }

}
