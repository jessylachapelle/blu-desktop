package handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APIConnector;

/**
 * Handles API calls to insert or delete transactions
 *
 * @author Jessy Lachapelle
 * @since 2016/07/24
 * @version 1.0
 */
public class TransactionHandler {

  /**
   * Default Constructor
   */
  public TransactionHandler() {}

  /**
   * Delete a transaction
   * @param copyId The id of the copy associated with the transaction
   * @param type The type of the transaction to delete
   * @return true If deletion is successful
   */
  public boolean delete(int copyId, String type) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("copy", copyId);
      data.put("type", type);

      req.put("function", "delete");
      req.put("object", "transaction");
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
   * Insert a transaction
   * @param memberNo The no of the member associated with the transaction
   * @param copyId The id of the copy associated with the transaction
   * @param type The type of transaction to insert
   * @return true If insert is successful
   */
  public boolean insert(int memberNo, int copyId, String type) {
    int[] copies = new int[1];
    copies[0] = copyId;
    return insert(memberNo, copies, type);
  }

  /**
   * Insert transactions on multiple copies
   * @param memberNo The no of the member associated with the transaction
   * @param copyIds The id of the copies associated with the transactions
   * @param type The type of transaction to insert
   * @return true If insert is successful
   */
  public boolean insert(int memberNo, int[] copyIds, String type) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray copies = new JSONArray();

    try {
      for (int copyId : copyIds) {
        copies.put(copyId);
      }

      data.put("member", memberNo);
      data.put("copies", copies);
      data.put("type", type);

      req.put("function", "insert");
      req.put("object", "transaction");
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
