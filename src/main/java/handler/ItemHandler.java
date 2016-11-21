package handler;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import api.APIConnector;
import model.item.*;
import model.transaction.Transaction;


/**
 * Permet de récupérer des informations dans la Base de données et de les
 * transformer en objet. Gère les propriétés des objets et les actions reliées
 * en faisant le lien avec la BD.
 *
 * @author Jessy
 * @version 1.1
 * @since 2016/19/07
 */
public class ItemHandler {

  private Item item;
  private static ArrayList<Category> categories;

  public ItemHandler() {
    item = new Item();

    if (categories == null || categories.size() == 0) {
      _initCategories();
    }
  }

  public boolean addItemReservation(int memberNo) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("member", memberNo);
    data.put("item", item.getId());

    req.put("object", "reservation");
    req.put("function", "insert");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0)  == 200) {
      int id = data.optInt("id", 0);
      item.addReservation(new Reservation(id, memberNo));
      return true;
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

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.delete(copyId, "SELL")) {
      item.getCopy(copyId).removeTransaction("SELL");
      item.getCopy(copyId).removeTransaction("SELL_PARENT");
      return true;
    }

    return false;
  }

  public boolean delete() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", item.getId());

    req.put("object", "item");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  public boolean deleteCopy(int copyId) {
    CopyHandler copyHandler = new CopyHandler();

    if (copyHandler.deleteCopy(copyId)) {
      item.removeCopy(copyId);
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
    data.put("type", Transaction.Type.RESERVATION);

    req.put("object", "transaction");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      getItem().getCopy(copyId).removeTransaction(Transaction.Type.RESERVATION);
      return true;
    }

    return false;
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
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      getItem().removeReservation(memberNo);
      return true;
    }

    return false;
  }

  public boolean exists(String ean13) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("ean13", ean13);

    req.put("object", "item");
    req.put("function", "exists");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  public ArrayList<Category> getCategories() {
    return categories;
  }

  public Item getItem() {
    return item;
  }

  public void initBook() {
    item = new Book();
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

  /**
   * Search for items with corresponding query
   * @param search The search query
   * @return A list of items
   */
  public ArrayList<Item> searchItem(String search, boolean outdated) {
    ArrayList<Item> items = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("search", search);

    if (outdated) {
      data.put("outdated", true);
    }

    json.put("function", "search");
    json.put("object", "item");
    json.put("data", data);

    JSONObject req = APIConnector.call(json);
    JSONArray itemArray = req.optJSONArray("data");

    if (itemArray != null) {
      for (int i = 0; i < itemArray.length(); i++) {
        JSONObject item = itemArray.optJSONObject(i);
        if (item != null) {
          items.add(item.optBoolean("is_book", false) ? new Book(item) : new Item(item));
        }
      }
    }

    return items;
  }

  public Item selectItem(int id) {
    JSONObject data = new JSONObject();
    data.put("id", id);
    return _selectItem(data);
  }

  public Item selectItem(String ean13) {
    return selectItem(ean13, false);
  }

  public Item selectItem(String ean13, boolean forCopy) {
    JSONObject data = new JSONObject();
    data.put("ean13", ean13);

    if (forCopy) {
      data.put("forCopy", true);
    }

    return _selectItem(data);
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public boolean updateComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", id);
    data.put("comment", comment.replaceAll("'", "''"));

    json.put("function", "update_comment");
    json.put("object", "item");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      item.setDescription(comment);
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

  public boolean updateStatus(int id, String status) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", id);
    data.put("status", status);

    req.put("object", "item");
    req.put("function", "updateStatus");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      ((Book) item).updateStatus(status, new Date());
      return true;
    }

    return false;
  }

  public boolean updateStorage(int id, String[] storage) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray storageArray = new JSONArray();

    for (String storageUnit : storage) {
      storageArray.put(storageUnit);
    }

    data.put("id", id);
    data.put("storage", storageArray);

    json.put("function", "update_storage");
    json.put("object", "item");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  private void _initCategories() {
    categories = new ArrayList<>();
    JSONObject req = new JSONObject();

    req.put("object", "category");
    req.put("function", "select");
    req.put("data", new JSONObject());

    JSONObject res = APIConnector.call(req);
    JSONArray data = res.optJSONArray("data");

    if (data != null) {
      for (int i = 0; i < data.length(); i++) {
        JSONObject c = data.optJSONObject(i);
        if (c != null) {
          categories.add(new Category(c));
        }
      }
    }
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
    this.item = item.optBoolean("is_book", false) ? new Book(item) : new Item(item);

    if (data.optInt("id", 0) > 0) {
      this.item.setId(data.getInt("id"));
    }

    JSONArray authorList = data.optJSONArray("author");
    if (authorList != null) {
      for (int i = 0; i < authorList.length(); i++) {
        JSONObject a = authorList.optJSONObject(i);
        if (a != null) {
          ((Book) this.item).addAuthor(new Author(a));
        }
      }
    }

    return data.optInt("code", 0) == 200;
  }

  private Item _selectItem(JSONObject data) {
    JSONObject req = new JSONObject();

    req.put("object", "item");
    req.put("function", "select");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null) {
      item = data.optBoolean("is_book", false) ? new Book(data) : new Item(data);
      return item;
    }

    return null;
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
        JSONObject a = authorList.optJSONObject(i);
        if (a != null) {
          ((Book) this.item).setAuthorId(new Author(a));
        }
      }
    }

    return data.optInt("code", 0) == 200;
  }
}
