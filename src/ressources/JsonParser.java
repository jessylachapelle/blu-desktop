package ressources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Permet de convertir des données JSON en d'autres formats
 *
 * @author Jessy Lachapelle
 * @since 14/11/2015
 * @version 0.2
 */
public class JsonParser {

  /**
   * Permet de convertir du JSON en HashMap
   *
   * @param json Données en JSON
   * @return Données associées par clé-valeur
   */
  public static HashMap toHashMap(JSONObject json) {
    HashMap map = new HashMap();
    Iterator keys = json.keys();

    while (keys.hasNext()) {
      try {
        String key = keys.next().toString();
        String value = json.getString(key);

        map.put(key, value);
      } catch (JSONException e) {
        Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, e);
        return map;
      }
    }

    return map;
  }

  public static ArrayList<HashMap> toHashMap(ArrayList<JSONObject> json) {
    ArrayList<HashMap> maps = new ArrayList<>();

    for (int noJsonO = 0; noJsonO < json.size(); noJsonO++) {
      HashMap map = new HashMap();
      Iterator keys = json.get(noJsonO).keys();

      while (keys.hasNext()) {
        try {
          String key = keys.next().toString();
          String value;

          if (!json.get(noJsonO).isNull(key)) {
            value = json.get(noJsonO).getString(key);
          } else {
            value = "";
          }

          map.put(key, value);

        } catch (JSONException e) {
          Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, e);
          return maps;
        }
      }

      maps.add(map);
    }

    return maps;
  }
  
  public static JSONObject toJSON(HashMap map) {
    JSONObject json = new JSONObject();
    Iterator keys = map.keySet().iterator();
    
    while(keys.hasNext()) {
      try {
        String key = keys.next().toString();
        json.append(key, map.get(key));
      } catch (JSONException e) {
        Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, e);
        return json;
      }
    }
    String test = json.toString();
    return json;
  }
}
