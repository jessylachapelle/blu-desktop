package handler;

import api.APIConnector;
import model.transaction.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Permet de reprendre des resultset et de les transformer en objet et de
 * transféré des objets de type Transaction à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 12/11/2015
 * @version 0.2
 */
@SuppressWarnings("unused")
public class TransactionHandler {
  // private Transaction transaction;
  private ArrayList<Transaction> transactions;

  /**
   * Constructeur par défaut
   */
  public TransactionHandler() {
    transactions = new ArrayList<>();
  }


  public ArrayList<Transaction> insert(int member, int copy, int type) {
    int[] copies = new int[1];
    copies[0] = copy;
    return insert(member, copies, type);
  }

  public ArrayList<Transaction> insert(int member, int[] copy, int type) {
    transactions.clear();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray copies = new JSONArray();

    try {
      for (int aCopy : copy) {
        copies.put(aCopy);
      }
      data.put("member", member);
      data.put("copies", copies);
      data.put("type", type);

      json.put("function", "insert");
      json.put("object", "transaction");
      json.put("data", data);

      JSONArray response = APIConnector.call(json).getJSONArray("data");

      for (int i = 0; i < response.length(); i++) {
        transactions.add(new Transaction(response.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return transactions;
  }

  public boolean delete(int id) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      json.put("function", "delete");
      json.put("object", "transaction");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean delete(int copyId, int type) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("copy_id", copyId);
      data.put("type", type);

      json.put("function", "delete");
      json.put("object", "transaction");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }
}
