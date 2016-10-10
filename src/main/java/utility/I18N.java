package utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Jessy Lachapelle
 * @since 16/19/07
 * @version 1.0
 */
public class I18N {
  private JSONObject i18n;

  public I18N() {
    _init(Settings.lang());
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

  @SuppressWarnings("ConstantConditions")
  private void _init(String lang) {
    String jsonString = "";
    String path = "i18n/" + lang + ".json";
    if (this.getClass().getClassLoader().getResource(path) != null) {
      File file = new File(this.getClass().getClassLoader().getResource(path).getFile());

      try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNext()) {
          jsonString += scanner.nextLine();
        }

        i18n = new JSONObject(jsonString);
        scanner.close();
      } catch (FileNotFoundException | JSONException e) {
        e.printStackTrace();
      }
    }
  }
}
