package model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Information about storage for items
 * @author Jessy Lachapelle
 * @since 13/07/2016
 */
@SuppressWarnings("unused")
public class Storage {
  private String code;
  private ArrayList<Item> content;

  /**
   * Constructeur de base
   */
  public Storage() {
    init();
  }

  public Storage(JSONObject json) {
    init();
    fromJSON(json);
  }

  public void init() {
    code = "";
    content = new ArrayList<>();
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
   * Attribue une valeur au code
   *
   * @param code Code de l'unite de rangement
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Ajoute un item dans l'unité de rangement
   *
   * @param item Un item
   */
  public void addItem(Item item) {
    this.content.add(item);
  }

  /**
   * Ajoute un array d'item dans l'unité de rangement
   *
   * @param item Un item
   */
  public void addItems(ArrayList<Item> item) {
    content.addAll(item);
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

  /**
   * Récupère tous les articles content dans l'unité de rangement
   *
   * @return content Une liste d'articles
   */
  public ArrayList<Item> getAllItems() {
    return content;
  }

  /**
   * Supprime un item de l'unité de rangement
   *
   * @param index L'index de l'item
   */
  public void deleteItem(int index) {
    content.remove(index);
  }

  /**
   * Supprime tous les articles content dans l'unité de rangement
   */
  public void clearItems() {
    content.clear();
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("code")) {
        code = json.getString("code");
      }

      if (json.has("content")) {
        JSONArray contents = json.getJSONArray("content");

        for(int i = 0; i < contents.length(); i++) {
          content.add(new Item(contents.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject storage = new JSONObject();
    JSONArray content = new JSONArray();

    try {
      storage.put("code", code);

      for (Item item : this.content) {
        content.put(item.toJSON());
      }

      storage.put("content", content);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return storage;
  }
}
