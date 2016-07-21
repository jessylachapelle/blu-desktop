package handler;

import api.APIConnector;
import model.item.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Permet de récupérer des informations dans la Base de données et de les
 * transformer en objet. Gère les propriétés des objets et les actions reliées
 * en faisant le lien avec la BD.
 *
 * @author Jessy
 * @version 1.1
 * @since 2016/19/07
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused"})
public class ItemHandler {

  private ArrayList<Item> items;
  private Item item;

  public ItemHandler() {
    item = null;
    items = new ArrayList<>();
  }

  public boolean updateComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment);

      json.put("function", "update_comment");
      json.put("object", "item");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addTransaction(int memberNo, int copyId, int type) {
    int id = 0;

    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(memberNo, copyId, type);

    return id;
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();
    return transactionHandler.delete(copyId, 2);
  }

  public boolean updateStorage(int id, String[] storage) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray storagejson = new JSONArray();

    try {
      for (String storageUnit : storage) {
        storagejson.put(storageUnit);
      }

      data.put("id", id);
      data.put("storage", storagejson);

      json.put("function", "update_storage");
      json.put("object", "item");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public Item getItem() {
    return item;
  }

  public ArrayList<Item> getItems() {
    return items;
  }

  /**
   * Add an item to the system or update it if already exists
   * @param item The item's information
   * @return The item
   */
  public Item addItem(JSONObject item) {
    int id = 0;

    try {
      if (item.has("id")) {
        id = item.getInt("id");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (id > 0) {
      return updateItem(item);
    }

    return insertItem(item);
  }

  public Item selectItem(int id) {
    try {
      JSONObject data = new JSONObject();
      return _selectItem(data.put("id", id));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Item selectItem(String ean13) {
    try {
      JSONObject data = new JSONObject();
      return _selectItem(data.put("ean13", ean13));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  private Item _selectItem(JSONObject data) {
    JSONObject json = new JSONObject();

    try {
      json.put("object", "item");
      json.put("function", "select");
      json.put("data", data);

      JSONObject response = APIConnector.call(json);
      JSONObject itemData = response.getJSONObject("data");

      if (itemData.has("isBook") && itemData.getBoolean("isBook")) {
        item = new Book(itemData);
      } else {
        item = new Item(itemData);
      }

      return item;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Item insertItem(JSONObject item) {
    JSONObject json = new JSONObject();

    try {
      json.put("object", "item");
      json.put("function", "insert");
      json.put("data", item);

      JSONObject res = APIConnector.call(json);
      JSONObject data = res.getJSONObject("data");
      item.put("id", data.getInt("id"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return new Item(item);
  }

  public Item updateItem(JSONObject item) {
    JSONObject json = new JSONObject();

    try {
      json.put("object", "item");
      json.put("function", "update");
      json.put("data", item);

      JSONObject res = APIConnector.call(json);
      JSONObject data = res.getJSONObject("data");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return new Item(item);
  }

  /**
   * Search for items with corresponding query
   * @param search The search query
   * @return A list of items
   */
  public ArrayList<Item> searchItem(String search, boolean outdated) {
    ArrayList<Item> items = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("search", search);

      if (outdated) {
        data.put("outdated", true);
      }

      json.put("function", "search");
      json.put("object", "item");
      json.put("data", data);

      JSONArray itemArray = APIConnector.call(json).getJSONArray("data");

      for (int i = 0; i < itemArray.length(); i++) {
        JSONObject item = itemArray.getJSONObject(i);

        if (item.has("is_book") && item.getBoolean("is_book")) {
          items.add(new Book(item));
        } else {
          items.add(new Item(item));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return items;
  }

  /**
   * Retourne toute les matières
   *
   * @return TOUTE LES MATIÈRES
   */
  public ArrayList<Subject> getSubjects() {
    JSONObject req = new JSONObject();
    ArrayList<Subject> subjects = new ArrayList<>();

    try {
      req.put("object", "subject");
      req.put("function", "select");

      JSONObject res = APIConnector.call(req);
      JSONArray data = res.getJSONArray("data");

      for (int i = 0; i < data.length(); i++) {
        subjects.add(new Subject(data.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return subjects;
  }

  public int addItemReservation(int memberNo, int itemId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();
    int id = 0;

    try {
      data.put("member", memberNo);
      data.put("item", itemId);

      req.put("object", "reservation");
      req.put("function", "insert");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");
      id = data.getInt("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return id;
  }

  public int addCopyReservation(int memberNo, int copyId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();
    int id = 0;

    try {
      data.put("member", memberNo);
      data.put("copy", copyId);
      data.put("type", "RESERVE");

      req.put("object", "transaction");
      req.put("function", "insert");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");
      id = data.getInt("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return id;
  }

  public boolean deleteReservation(int memberNo, int itemId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("member", memberNo);
      data.put("item", itemId);

      req.put("object", "reservation");
      req.put("function", "delete");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      return data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Supprime la réservation pour un exemplaire et un member dans la BD
   * @param copyId Le id de l'exemplaire
   * @return Vrai si la supression est un succès
   */
  public boolean deleteReservation(int copyId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("copy", copyId);
      data.put("type", "RESERVE");

      req.put("object", "transaction");
      req.put("function", "delete");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      return data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean setStatus(int id, String status) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("status", status.toUpperCase());

      req.put("object", "item");
      req.put("function", "setStatus");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");
      return data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }
}
