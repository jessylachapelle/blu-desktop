package handler;

import api.APIConnector;
import model.membre.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Permet de reprendre des ResulSet et de les transformer en objet et de
 * transférer des objets de type Membre à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 29/10/2015
 * @version 0.3
 */
public class MemberHandler {
  private ArrayList<Membre> members;
  private Membre member;

  /**
   * Constructeur par défaut
   */
  public MemberHandler() {
    this.member = new Membre();
    this.members = new ArrayList<>();
  }

  public void setMember(int no) {
    member = selectMember(no);
  }

  public void setMember(Membre member) {
    this.member = member;
  }

  public Membre getMember(int no) {
    selectMember(no);
    return member;
  }

  public Membre getMember() {
    return member;
  }

  public Membre selectMember(int no) {
    Membre member = new Membre();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "select");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject memberData = APIConnector.call(json).getJSONObject("data");
      member.fromJSON(memberData);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return member;
  }

  /**
   * Search for members with corresponding query
   * @param search The search query
   * @param deactivated True if want to search in deactivated accounts
   * @return A List of members
   */
  public ArrayList<Membre> searchMembers(String search, boolean deactivated) {
    ArrayList<Membre> members = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("search", search);

      if (deactivated) {
        data.put("deactivated", deactivated);
      }

      json.put("function", "search");
      json.put("object", "membre");
      json.put("data", data);

      JSONArray memberArray = APIConnector.call(json).getJSONArray("data");

      for(int i = 0; i < memberArray.length(); i++) {
        members.add(new Membre(memberArray.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return members;
  }

  public Membre insertMember(Membre member) {
    String comment = null;
    if (member.getAccount().getComments().size() > 0) {
      comment = member.getAccount().getComments().get(0).getComment();
    }

    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray phones = new JSONArray();
    JSONObject response;

    try {
      data.append("no", Integer.toString(member.getNo()));
      data.append("prenom", member.getFirstName());
      data.append("nom", member.getLastName());
      data.append("code_postal", member.getZip());
      data.append("ville", member.getCity());
      data.append("province", member.getState());
      data.append("courriel", member.getEmail());

      if (comment != null) {
        data.append("commentaire", comment);
      }

      for(int i = 0; i < member.getPhone().length; i++) {
        if (!member.getPhone(i).getNumber().equals("")) {
          JSONObject phone = new JSONObject();

          phone.append("numero", member.getPhone(i).getNumber());
          phone.append("note", member.getPhone(i).getNote());

          phones.put(phone.toString());
        }
      }

      data.append("telephone", phones.toString());
      json.append("object", "member");
      json.append("function", "insert");
      json.append("data", data.toString());

      response = APIConnector.call(json);

      for(int i = 0; i < member.getPhone().length; i++) {
        if (!member.getPhone(i).getNumber().equals("")) {
          int phoneId = response.getInt(member.getPhone(i).getNumber());
          member.getPhone(i).setId(phoneId);
        }
      }

      if (comment != null) {
        int commentId = response.getInt("commentaireId");
        member.getAccount().getComments().get(0).setId(commentId);
      }
    } catch (JSONException ex) {
      Logger.getLogger(MemberHandler.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }

    return member;
  }

  public Membre updateMember(Membre member) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONObject jsonMember = new JSONObject();
    JSONArray phones = new JSONArray();
    JSONArray comments = new JSONArray();
    JSONObject response;

    try {
      data.append("no", Integer.toString(member.getNo()));
      jsonMember.append("first_name", member.getFirstName());
      jsonMember.append("last_name", member.getLastName());
      jsonMember.append("zip", member.getZip());
      jsonMember.append("city", member.getCity());
      jsonMember.append("state", member.getState());
      jsonMember.append("email", member.getEmail());

      for(int i = 0; i < member.getPhone().length; i++) {
        if (member.getPhone(i) != null) {
          JSONObject phone = new JSONObject();

          if (member.getPhone(i).getId() != 0) {
            phone.append("id", member.getPhone(i).getId());
          }

          phone.append("number", member.getPhone(i).getNumber());
          phone.append("note", member.getPhone(i).getNote());

          phones.put(phone.toString());
        }
      }

      jsonMember.append("phone", phones);
      data.append("member", jsonMember);
      json.append("object", "member");
      json.append("function", "update");
      json.append("data", data.toString());

      response = APIConnector.call(json);
    } catch (JSONException ex) {
      Logger.getLogger(MemberHandler.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }

    return member;
  }

  public boolean deleteMember() {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      json.put("function", "delete");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      if (response.has("code")) {
        return response.getInt("code") == 200;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean renewAccount(int no) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "renew");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      if (response.has("code")) {
        return response.getInt("code") == 200;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addComment(String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", member.getNo());
      data.put("comment", comment);

      json.put("function", "insert_comment");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");
      return response.getInt("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return 0;
  }

  public boolean editComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment);

      json.put("function", "update_comment");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
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
      json.put("function", "delete_comment");
      json.put("object", "member");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addTransaction(int id, int type) {
    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(member.getNo(), id, type).get(0);
    return 0;
  }

  public int addTransaction(int[] copies, int type) {
    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(member.getNo(), copies, type).get(0);
    return 0;
  }

  public boolean updateCopyPrice(int copyId, double price) {
    CopyHandler copyHandler = new CopyHandler();
    return copyHandler.updateCopyPrice(copyId, price);
  }

  public boolean deleteCopy(int copyId) {
    CopyHandler copyHandler = new CopyHandler();
    return copyHandler.deleteCopy(copyId);
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();
    return transactionHandler.delete(copyId, 2);
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
