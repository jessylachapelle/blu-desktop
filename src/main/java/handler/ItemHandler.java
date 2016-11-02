package handler;

import api.APIConnector;
import model.item.*;
import java.util.ArrayList;

import model.transaction.Transaction;
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
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused", "WeakerAccess"})
public class ItemHandler {

  private ArrayList<Item> items;
  private Item item;
  private static ArrayList<Category> categories;

  public ItemHandler() {
    item = new Item();
    items = new ArrayList<>();

    if (categories == null || categories.size() == 0) {
      _initCategories();
    }
  }

  public void initBook() {
    item = new Book();
  }

  public boolean updateComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment.replaceAll("'", "''"));

      json.put("function", "update_comment");
      json.put("object", "item");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      if (response.has("code") && response.getInt("code") == 200) {
        item.setDescription(comment);
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean addTransaction(int memberNo, int copyId, String type) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.insert(memberNo, copyId, type)) {
      item.getCopy(copyId).addTransaction(type);
      return true;
    }

    return false;
  }

  public boolean updateCopyPrice(int copyId, double price) {
    CopyHandler copyHandler = new CopyHandler();

    if (copyHandler.updateCopyPrice(copyId, price)) {
      item.getCopy(copyId).setPrice(price);
      return true;
    }

    return false;
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.delete(copyId, "SELL")) {
      item.getCopy(copyId).removeTransaction("SELL");
      item.getCopy(copyId).removeTransaction("SELL_PARENT");
      return true;
    }

    return false;
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
  public boolean saveItem(JSONObject item) {
    if (this.item.getId() > 0) {
      return _updateItem(item);
    }

    return _insertItem(item);
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
    JSONObject req = new JSONObject();

    try {
      req.put("object", "item");
      req.put("function", "select");
      req.put("data", data);

      JSONObject response = APIConnector.call(req);
      JSONObject itemData = response.getJSONObject("data");

      if (itemData.has("is_book") && itemData.getBoolean("is_book")) {
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

  private boolean _insertItem(JSONObject item) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("item", item);

    req.put("object", "item");
    req.put("function", "insert");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data") != null ? res.getJSONObject("data") : new JSONObject();

    item.remove("author");
    if (item.optBoolean("is_book", false)) {
      this.item = new Book(item);
    } else {
      this.item = new Item(item);
    }

    if (data.optInt("id", 0) > 0) {
      this.item.setId(data.getInt("id"));
    }

    JSONArray authorList = data.optJSONArray("author");
    if (authorList != null) {
      for (int i = 0; i < authorList.length(); i++) {
        ((Book) this.item).addAuthor(new Author(authorList.getJSONObject(i)));
      }
    }

    return data.optInt("code", 0) == 200;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  private boolean _updateItem(JSONObject item) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", this.item.getId());
    data.put("item", item);

    req.put("object", "item");
    req.put("function", "update");
    req.put("data", data);


    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data") != null ? res.getJSONObject("data") : new JSONObject();

    JSONArray authorList = item.optJSONArray("author");
    item.remove("author");
    this.item.fromJSON(item);

    if (authorList != null) {
      for (int i = 0; i < authorList.length(); i++) {
        JSONObject author = authorList.getJSONObject(i);

        if (author.has("id") && !author.has("first_name") && !author.has("last_name")) {
          ((Book) this.item).deleteAuthor(author.getInt("id"));
        } else if (author.has("id")) {
          ((Book) this.item).updateAuthor(new Author(author));
        }
      }
    }

    authorList = data.optJSONArray("author");
    if (authorList != null) {
      for (int i = 0; i < authorList.length(); i++) {
        ((Book) this.item).setAuthorId(new Author(authorList.getJSONObject(i)));
      }
    }

    return data.optInt("code", 0) == 200;
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
      req.put("data", new JSONObject());

      JSONObject res = APIConnector.call(req);

      if (res.has("data") && res.get("data") instanceof JSONArray) {
        JSONArray data = res.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
          subjects.add(new Subject(data.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return subjects;
  }

  public ArrayList<Category> getCategories() {
    return categories;
  }

  private void _initCategories() {
    categories = new ArrayList<>();
    JSONObject req = new JSONObject();

    try {
      req.put("object", "category");
      req.put("function", "select");
      req.put("data", new JSONObject());

      JSONObject res = APIConnector.call(req);

      if (res.has("data") && res.get("data") instanceof JSONArray) {
        JSONArray data = res.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
          categories.add(new Category(data.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public boolean exists(String ean13) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("ean13", ean13);

      req.put("object", "item");
      req.put("function", "exists");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);

      if (res.has("data") && res.get("data") instanceof JSONObject) {
        data = res.getJSONObject("data");
        return data.has("code") && data.getInt("code") == 200;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean addItemReservation(int memberNo) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("member", memberNo);
      data.put("item", item.getId());

      req.put("object", "reservation");
      req.put("function", "insert");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");
      int id = data.optInt("id", 0);

      if (data.optInt("code", 0)  == 200) {
        item.addReservation(new Reservation(id, memberNo));
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
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

  public boolean deleteItemReservation(int memberNo) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("member", memberNo);
    data.put("item", item.getId());

    req.put("object", "reservation");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.getJSONObject("data");

    if (data.optInt("code", 0) == 200) {
      getItem().removeReservation(memberNo);
      return true;
    }

    return false;
  }

  /**
   * Supprime la réservation pour un exemplaire et un member dans la BD
   * @param copyId Le id de l'exemplaire
   * @return Vrai si la supression est un succès
   */
  public boolean deleteCopyReservation(int copyId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("copy", copyId);
    data.put("type", Transaction.RESERVATION);

    req.put("object", "transaction");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.getJSONObject("data");

    if (data.optInt("code", 0) == 200) {
      getItem().getCopy(copyId).removeTransaction(Transaction.RESERVATION);
      return true;
    }

    return false;
  }

  public boolean deleteCopy(int copyId) {
    CopyHandler copyHandler = new CopyHandler();

    if (copyHandler.deleteCopy(copyId)) {
      item.removeCopy(copyId);
      return true;
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

  public boolean delete() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", item.getId());

      req.put("object", "item");
      req.put("function", "delete");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.optJSONObject("data");

      return data != null && data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
      return false;
    }
  }
}
