package handler;

import org.json.JSONArray;
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

    data.put("copy", copyId);
    data.put("type", type);

    req.put("function", "delete");
    req.put("object", "transaction");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
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
    return _insert(memberNo, copies, type);
  }

  /**
   * Insert transactions on multiple copies
   * @param memberNo The no of the member associated with the transaction
   * @param copyIds The id of the copies associated with the transactions
   * @param type The type of transaction to insert
   * @return true If insert is successful
   */
  private boolean _insert(int memberNo, int[] copyIds, String type) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray copies = new JSONArray();

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
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }
}
