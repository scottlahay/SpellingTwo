package scott.spellingtwo.domain;

import com.scott.spellingdomain.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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
            String temp = "";
        }
        return null;
    }

}
