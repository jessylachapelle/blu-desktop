package handler;

import api.APIConnector;
import model.member.*;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Permet de reprendre des ResulSet et de les transformer en objet et de
 * transférer des objets de type Member à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 29/10/2015
 * @version 0.3
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
public class MemberHandler {
  private ArrayList<Member> members;
  private Member member;
  private static ArrayList<String> states;

  /**
   * Constructeur par défaut
   */
  public MemberHandler() {
    this.member = new Member();
    this.members = new ArrayList<>();

    if (states == null || states.size() == 0) {
      _initStates();
    }
  }

  public void setMember(int no) {
    member = _selectMember(no);
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public Member getMember(int no) {
    _selectMember(no);
    return member;
  }

  public Member getMember() {
    return member;
  }

  private Member _selectMember(int no) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);

      req.put("function", "select");
      req.put("object", "member");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);

      if (res.has("data") && res.get("data") instanceof JSONObject) {
        data = res.getJSONObject("data");
        member = new Member(data);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      member = new Member();
    }

    return member;
  }

  /**
   * Search for members with corresponding query
   * @param search The search query
   * @param deactivated True if want to search in deactivated accounts
   * @return A List of members
   */
  ArrayList<Member> searchMembers(String search, boolean deactivated) {
    ArrayList<Member> members = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("search", search);

      if (deactivated) {
        data.put("deactivated", true);
      }

      json.put("function", "search");
      json.put("object", "member");
      json.put("data", data);

      JSONObject req = APIConnector.call(json);

      JSONArray memberArray = req.getJSONArray("data");

      for (int i = 0; i < memberArray.length(); i++) {
        members.add(new Member(memberArray.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return members;
  }

  public Member insertMember(JSONObject memberData) {
    JSONObject req = new JSONObject();

    try {
      req.put("object", "member");
      req.put("function", "insert");
      req.put("data", memberData);

      JSONObject res = APIConnector.call(req);
      JSONObject data = res.getJSONObject("data");

      member.fromJSON(memberData);

      if (data.has("city")) {
        JSONObject city = data.getJSONObject("city");
        member.setCityId(city.getInt("id"));
      }

      if (data.has("comment")) {
        JSONObject comment = data.getJSONObject("comment");
        member.getAccount().getComments().get(0).setId(comment.getInt("id"));
      }

      if (data.has("phone")) {
        JSONArray phones = data.getJSONArray("phone");

        for (int i = 0; i < phones.length(); i++) {
          JSONObject phone = phones.getJSONObject(i);

          if (member.getPhone(0).getNumber().equals(phone.getString("number"))) {
            member.getPhone(0).setId(phone.getInt("id"));
          } else {
            member.getPhone(1).setId(phone.getInt("id"));
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return member;
  }

  public Member updateMember(JSONObject memberData) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      data.put("member", memberData);

      req.put("object", "member");
      req.put("function", "update");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      if (data.optInt("code") == 500) {
        return null;
      }

      member.fromJSON(memberData);

      if (data.has("city")) {
        JSONObject city = data.getJSONObject("city");
        member.setCityId(city.getInt("id"));
      }

      if (data.has("phone")) {
        JSONArray phones = data.getJSONArray("phone");

        for (int i = 0; i < phones.length(); i++) {
          JSONObject phone = phones.getJSONObject(i);

          if (phone.has("number")) {
            if (member.getPhone(0).getNumber().equals(phone.getString("number"))) {
              member.getPhone(0).setId(phone.getInt("id"));
            } else {
              member.getPhone(1).setId(phone.getInt("id"));
            }
          } else {
            member.removePhone(phone.getInt("id"));
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return member;
  }

  public boolean deleteMember() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      req.put("function", "delete");
      req.put("object", "member");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      return data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean renewAccount() {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      json.put("function", "renew");
      json.put("object", "member");
      json.put("data", data);

      JSONObject res = APIConnector.call(json);
      data = res.getJSONObject("data");

      if (data.has("code") && data.getInt("code") == 200) {
        member.getAccount().setLastActivity(new Date());
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean addComment(String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      data.put("comment", comment);

      json.put("function", "insert_comment");
      json.put("object", "member");
      json.put("data", data);

      JSONObject res = APIConnector.call(json);
      data = res.getJSONObject("data");

      if (data.has("id")) {
        member.getAccount().addComment(new Comment(data.getInt("id"), comment));
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean updateComment(int id, String comment) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment);

      req.put("function", "update");
      req.put("object", "comment");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      if (data.has("code") && data.getInt("code") == 200) {
        member.getAccount().editComment(id, comment, "");
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean deleteComment(int id) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      json.put("function", "delete");
      json.put("object", "comment");
      json.put("data", data);

      JSONObject res = APIConnector.call(json);
      data = res.getJSONObject("data");

      if (data.has("code") && data.getInt("code") == 200) {
        member.getAccount().removeComment(id);
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean addTransaction(int copyId, String type) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.insert(member.getNo(), copyId, type)) {
      member.getAccount().getCopy(copyId).addTransaction(type);
      return true;
    }

    return false;
  }

  public boolean addTransaction(int copyId, String type, int memberNo) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.insert(memberNo, copyId, type)) {
      member.getAccount().getCopy(copyId).addTransaction(type);
      return true;
    }

    return false;
  }

  public boolean addTransaction(int[] copies, String type) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.insert(member.getNo(), copies, type)) {
      for (int copy : copies) {
        member.getAccount().getCopy(copy).addTransaction(type);
      }

      return true;
    }

    return false;
  }

  public boolean pay() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      req.put("function", "pay");
      req.put("object", "member");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      if (data.has("code") && data.getInt("code") == 200) {
        member.getAccount().pay();
        return true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean updateCopyPrice(int copyId, double price) {
    CopyHandler copyHandler = new CopyHandler();

    if (copyHandler.updateCopyPrice(copyId, price)) {
      member.getAccount().getCopy(copyId).setPrice(price);
      return true;
    }

    return false;
  }

  public boolean deleteCopy(int copyId) {
    CopyHandler copyHandler = new CopyHandler();

    if (copyHandler.deleteCopy(copyId)) {
      member.getAccount().removeCopy(copyId);
      return true;
    }

    return false;
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();

    if (transactionHandler.delete(copyId, "SELL")) {
      member.getAccount().getCopy(copyId).removeTransaction("SELL");
      member.getAccount().getCopy(copyId).removeTransaction("SELL_PARENT");
      return true;
    }

    return false;
  }

  public ArrayList<String> getStates() {
    return states;
  }

  private void _initStates() {
    JSONObject req = new JSONObject();
    states = new ArrayList<>();

    try {
      req.put("object", "state");
      req.put("function", "select");
      req.put("data", new JSONObject());

      JSONObject res = APIConnector.call(req);
      JSONArray data = res.getJSONArray("data");

      for (int i = 0; i < data.length(); i++) {
        states.add(data.getString(i));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public boolean exist(int no) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "exist");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }
}
