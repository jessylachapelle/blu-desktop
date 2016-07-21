package ressources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Jessy Lachapelle
 * @since 16/19/07
 * @version 1.0
 */
public class I18N {
  private static final String DEFAULT_LANG = "fr";
  private JSONObject i18n;

  public I18N() {
    _init(DEFAULT_LANG);
  }

  public I18N(String lang) {
    _init(lang);
  }

  public String getString(String key) {
    JSONObject json = i18n;
    String[] keys = key.split("\\.");

    try {
      for (int i = 0; i < keys.length; i++) {
        if (json.has(keys[i])) {
          if (i != keys.length - 1) {
            json = json.getJSONObject(keys[i]);
          } else {
            return json.getString(keys[i]);
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return key;
  }

  private void _init(String lang) {
    String filePath = "src/files/i18n/" + lang + ".json";
    File file = new File(filePath);

    if (file.exists()) {
      try {
        InputStream inputStream = new FileInputStream(filePath);
        Scanner scanner = new Scanner(inputStream);
        i18n = new JSONObject(scanner.nextLine());
      } catch (FileNotFoundException | JSONException e) {
        e.printStackTrace();
      }
    } else {
      i18n = new JSONObject();
    }
  }
}
