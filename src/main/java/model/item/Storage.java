package model.item;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Information about storage for items
 * @author Jessy Lachapelle
 * @since 13/07/2016
 */
public class Storage {
  private String code;
  private ArrayList<Item> content;

  /**
   * Constructeur de base
   */
  public Storage() {
    init();
  }

  public Storage(String code) {
    this.code = code;
  }

  public void fromJSON(JSONObject storage) {
    code = storage.optString("code", code);

    JSONArray content = storage.optJSONArray("content");
    if (content != null) {
      this.content.clear();

      for(int i = 0; i < content.length(); i++) {
        JSONObject item = content.optJSONObject(i);
        if (item != null) {
          this.content.add(new Item(item));
        }
      }
    }
  }

  /**
   * Récupère le code
   *
   * @return code Code de l'unite de rangement
   */
  public String getCode() {
    return code;
  }

  /**
   * Récupère un item de l'unité de rangement
   *
   * @param index L'index de l'item
   * @return item Item à l'index
   */
  public Item getItem(int index) {
    return content.get(index);
  }

  public void init() {
    code = "";
    content = new ArrayList<>();
  }

  /**
   * Attribue une valeur au code
   *
   * @param code Code de l'unite de rangement
   */
  public void setCode(String code) {
    this.code = code;
  }

  public JSONObject toJSON() {
    JSONObject storage = new JSONObject();
    JSONArray content = new JSONArray();

    storage.put("code", code);

    for (Item item : this.content) {
      content.put(item.toJSON());
    }

    storage.put("content", content);

    return storage;
  }
}
