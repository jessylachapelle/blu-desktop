package utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Handle app settings
 * @author Jessy Lachapelle
 * @since 2016-10-01
 * @version 1.0
 */
public class Settings {
  private static final String FILE = "settings.json";
  private static JSONObject settings;

  public Settings() {
    if (getClass().getClassLoader().getResource(FILE) != null) {
      File file = new File(getClass().getClassLoader().getResource(FILE).getFile());

      try (Scanner scanner = new Scanner(file)) {
        String jsonString = "";

        while (scanner.hasNext()) {
          jsonString += scanner.nextLine();
        }

        settings = new JSONObject(jsonString);
        scanner.close();
      } catch (FileNotFoundException | JSONException e) {
        e.printStackTrace();
      }
    }
  }

  static public String lang() {
    if (settings.has("lang")) {
      return settings.getString("lang");
    }

    String systemLang = Locale.getDefault().toString().split("_")[0];
    JSONArray supportedLang = settings.getJSONArray("supported_lang");
    if (supportedLang.toList().contains(systemLang)) {
      return systemLang;
    }

    return settings.getString("default_lang");
  }

  static public ArrayList<String> supportedLang() {
    JSONArray jsonArray = settings.getJSONArray("supported_lang");
    ArrayList<String> supportedLang = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      supportedLang.add(jsonArray.getString(i));
    }

    return supportedLang;
  }

  static public void updateLang(String lang) {
    if (supportedLang().contains(lang)) {
      settings.remove("lang");
      settings.put("lang", lang);
      updateSettings();
    }
  }

  static public String scanFirstChar() {
    return settings.getString("scan_first_char");
  }

  static public String scanLastChar() {
    return settings.getString("scan_last_char");
  }

  static public void updateScanChars(String scanFirstChar, String scanLastChar) {
    settings.remove("scan_first_char");
    settings.remove("scan_last_char");

    settings.put("scan_first_char", scanFirstChar);
    settings.put("scan_last_char", scanLastChar);

    if (updateSettings()) {
      Dialog.confirmation("Les paramètres ont été mis à jour");
    } else {
      Dialog.confirmation("Une erreur est survenue lors de la mise à jour des paramètres");
    }
  }

  static public String apiKey() {
    return settings.has("api_key") ? settings.getString("api_key") : "";
  }

  static public String apiUrl() {
    return settings.has("api_url") ? settings.getString("api_url") : "";
  }

  static private boolean updateSettings() {
    try {
      File file = new File(Settings.class.getClassLoader().getResource(FILE).getFile());
      FileWriter fileWriter = new FileWriter(file, false);

      fileWriter.write(settings.toString());
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }
}
