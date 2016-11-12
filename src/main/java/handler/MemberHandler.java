package handler;

import api.APIConnector;
import model.item.Copy;
import model.member.*;
import java.util.ArrayList;
import java.util.Date;

import model.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Permet de reprendre des ResulSet et de les transformer en objet et de
 * transférer des objets de type Member à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 29/10/2015
 * @version 0.3
 */
@SuppressWarnings("unused")
public class MemberHandler {
  private Member member;
  private static ArrayList<String> states;

  /**
   * Constructeur par défaut
   */
  public MemberHandler() {
    this.member = new Member();

    if (states == null || states.size() == 0) {
      _initStates();
    }
  }

  public void setMember(int no) {
    member = _selectMember(no);
  }

  public void setMember(String email) {
    member = _selectMember(email);
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
    JSONObject data = new JSONObject();
    data.put("no", no);
    return _selectMember(data);
  }

  private Member _selectMember(String email) {
    JSONObject data = new JSONObject();
    data.put("email", email);
    return _selectMember(data);
  }

  private Member _selectMember(JSONObject data) {
    JSONObject req = new JSONObject();

    req.put("function", "select");
    req.put("object", "member");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data == null) {
      return new Member();
    }

    member = data.optBoolean("is_parent", false) ? new StudentParent(data) : new Member(data);
    return member;
  }

  /**
   * Search for members with corresponding query
   * @param search The search query
   * @param deactivated True if want to search in deactivated accounts
   * @return A List of members
   */
  ArrayList<Member> searchMembers(String search, boolean deactivated, boolean parentOnly) {
    ArrayList<Member> members = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("search", search);

    if (deactivated) {
      data.put("deactivated", true);
    }

    if (parentOnly) {
      data.put("is_parent", true);
    }

    json.put("function", "search");
    json.put("object", "member");
    json.put("data", data);

    JSONObject req = APIConnector.call(json);
    JSONArray memberArray = req.optJSONArray("data");

    if (memberArray != null) {
      for (int i = 0; i < memberArray.length(); i++) {
        JSONObject m = memberArray.optJSONObject(i);
        if (m != null) {
          members.add(new Member(m));
        }
      }
    }

    return members;
  }

  public Member insertMember(JSONObject memberData) {
    JSONObject req = new JSONObject();

    req.put("object", "member");
    req.put("function", "insert");
    req.put("data", memberData);

    JSONObject res = APIConnector.call(req);
    JSONObject data = res.optJSONObject("data");

    if (data == null) {
      return null;
    }

    member.fromJSON(memberData);

    JSONObject  city = data.optJSONObject("city");
    if (city != null) {
      member.setCityId(city.optInt("id", 0));
    }

    JSONObject comment = data.optJSONObject("comment");
    if (comment != null) {
      member.getAccount().getComments().get(0).setId(comment.optInt("id", 0));
    }

    JSONArray phones = data.optJSONArray("phone");
    if (phones != null) {
      for (int i = 0; i < phones.length(); i++) {
        JSONObject phone = phones.optJSONObject(i);
        if (phone != null) {
          if (member.getPhone(0).getNumber().equals(phone.getString("number"))) {
            member.getPhone(0).setId(phone.getInt("id"));
          } else {
            member.getPhone(1).setId(phone.getInt("id"));
          }
        }
      }
    }

    return member;
  }

  public Member updateMember(JSONObject memberData) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("no", member.getNo());
    data.put("member", memberData);

    req.put("object", "member");
    req.put("function", "update");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.fromJSON(memberData);

      JSONObject city = data.optJSONObject("city");
      if (city != null) {
        member.setCityId(city.optInt("id", 0));
      }

      JSONArray phones = data.optJSONArray("phone");
      if (phones != null) {
        for (int i = 0; i < phones.length(); i++) {
          JSONObject phone = phones.optJSONObject(i);
          if (phone != null) {
            if (phone.has("number")) {
              if (member.getPhone(0).getNumber().equals(phone.optString("number", ""))) {
                member.getPhone(0).setId(phone.optInt("id", 0));
              } else {
                member.getPhone(1).setId(phone.optInt("id", 0));
              }
            } else {
              member.removePhone(phone.optInt("id", 0));
            }
          }
        }
      }

      return member;
    }

    return null;
  }

  public boolean deleteMember() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("no", member.getNo());
    req.put("function", "delete");
    req.put("object", "member");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");
    return data != null && data.optInt("code", 0) == 200;
  }

  public boolean renewAccount() {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("no", member.getNo());
    json.put("function", "renew");
    json.put("object", "member");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.getAccount().setLastActivity(new Date());
      return true;
    }

    return false;
  }

  public boolean addComment(String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("no", member.getNo());
    data.put("comment", comment);

    json.put("function", "insert_comment");
    json.put("object", "member");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    if (data != null && data.has("id")) {
      member.getAccount().addComment(new Comment(data.optInt("id", 0), comment));
      return true;
    }

    return false;
  }

  public boolean updateComment(int id, String comment) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", id);
    data.put("comment", comment);

    req.put("function", "update");
    req.put("object", "comment");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.getAccount().editComment(id, comment, "");
      return true;
    }

    return false;
  }

  public boolean deleteComment(int id) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", id);
    json.put("function", "delete");
    json.put("object", "comment");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.getAccount().removeComment(id);
      return true;
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

    data.put("no", member.getNo());
    req.put("function", "pay");
    req.put("object", "member");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.getAccount().pay();
      return true;
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

    req.put("object", "state");
    req.put("function", "select");
    req.put("data", new JSONObject());

    JSONObject res = APIConnector.call(req);
    JSONArray data = res.optJSONArray("data");

    if (data != null) {
      for (int i = 0; i < data.length(); i++) {
        states.add(data.getString(i));
      }
    }
  }

  public boolean exist(int no) {
    JSONObject data = new JSONObject();
    data.put("no", no);
    return exist(data);
  }

  public boolean exist(String email) {
    JSONObject data = new JSONObject();
    data.put("email", email);
    return exist(data);
  }

  private boolean exist(JSONObject data) {
    JSONObject json = new JSONObject();

    json.put("function", "exist");
    json.put("object", "member");
    json.put("data", data);

    JSONObject res = APIConnector.call(json);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  public boolean deleteItemReservation(int itemId) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("member", member.getNo());
    data.put("item", itemId);

    req.put("object", "reservation");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      member.getAccount().removeItemReservation(itemId);
      return true;
    }

    return false;
  }

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
      member.getAccount().removeCopyReservation(copyId);
      return true;
    }

    return false;
  }

  public boolean donate() {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray copyList = new JSONArray();
    ArrayList<Copy> copies = new ArrayList<>();

    copies.addAll(member.getAccount().getAvailable());
    copies.addAll(member.getAccount().getSold());

    for (Copy copy : copies) {
      copyList.put(copy.getId());
    }

    data.put("member", member.getNo());
    data.put("copies", copyList);
    data.put("type", Transaction.Type.DONATION);

    req.put("data", data);
    req.put("object", "transaction");
    req.put("function", "insert");

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      for (Copy copy : copies) {
        member.getAccount().removeCopy(copy.getId());
      }

      return true;
    }

    return false;
  }
}
