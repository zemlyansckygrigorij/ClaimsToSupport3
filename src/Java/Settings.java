/**
 * класс для получения настроек из json-файла
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * https://www.chillyfacts.com/java-send-json-request-read-json-response/
 * работа с json файлами
 * библиотеки java-json.jar  и org.apache.commons.io.jar
 * */

package Java;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Settings {

    private static Map<String, String> settings = new HashMap<String, String>();
    static{
        String path =System.getProperty("user.dir");

        String fileJson = path+ "/localSetting.json";
        String json = "{ \"method\" : \"guru.test\", \"params\" : [ \"jinu awad\" ], \"id\" : 123 }";
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(fileJson));
            String result = IOUtils.toString(in, "UTF-8");

            JSONObject jsonData = new JSONObject(result);
            settings.put("serverPath", jsonData.getString("serverPath"));
            settings.put("pathTeamviewer", jsonData.getString("pathTeamviewer"));

            settings.put("eMailFrom", jsonData.getString("eMailFrom"));
            settings.put("password", jsonData.getString("password"));
            settings.put("eMailTo", jsonData.getString("eMailTo"));

            settings.put("dbName", jsonData.getString("dbName"));
            settings.put("userName", jsonData.getString("userName"));
            settings.put("host", jsonData.getString("host"));
            settings.put("port", jsonData.getString("port"));
            settings.put("passwordDB", jsonData.getString("passwordDB"));

            settings.put("mailSmtpAuth", jsonData.getString("mailSmtpAuth"));
            settings.put("mailSmtpStarttlsEnable", jsonData.getString("mailSmtpStarttlsEnable"));
            settings.put("mailSmtpHost", jsonData.getString("mailSmtpHost"));
            settings.put("mailSmtpPort", jsonData.getString("mailSmtpPort"));

            settings.put("pathTeamviewer", jsonData.getString("pathTeamviewer"));
            in.close();

         } catch (FileNotFoundException e) {
   			System.out.println(e);
            Settings.writeError(e);
   		} catch (JSONException e) {
            e.printStackTrace();
            Settings.writeError(e);
        } catch (IOException e) {
            e.printStackTrace();
            Settings.writeError(e);
        }

    }
    public static Map<String, String> getSettings(){
        return  settings;
    }



    public static void writeError(Exception e){
        String path = System.getProperty("user.dir");
        String filename= path+"\\programmError.txt";
        File file = new File(path+"\\programmError.txt");
        if(!file.exists()){
            try {
                Files.createFile(Paths.get(path+"\\programmError.txt"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(filename));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace(pw);
        pw.close();

    }
}
//    Settings.writeError(e);