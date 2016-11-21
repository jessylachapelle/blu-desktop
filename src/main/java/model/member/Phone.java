package model.member;

import org.json.JSONObject;

/**
 * Un numéro de téléphone avec une note
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class Phone {
  private int id;
  private String number;
  private String note;

  /**
   * Constructeur par défaut, crée objet à valeur null
   */
  public Phone() {
    init();
  }

  public Phone(JSONObject json) {
    init();
    fromJSON(json);
  }

  @Override
  public String toString() {
    String phone = "";

    if (number.isEmpty()) {
      return phone;
    }

    phone = number.substring(0, 3) + " " + number.substring(3, 6) + "-" + number.substring(6, 10);

    if (!note.isEmpty()) {
      phone += " (" + note + ")";
    }

    return phone;
  }

  public void fromJSON(JSONObject phone) {
    id = phone.optInt("id", id);
    number = phone.optString("number", number);
    note = phone.optString("note", note);
  }

  public int getId() {
    return id;
  }

  /**
   * Accède à la note associée au numéro
   *
   * @return note La note associée au numéro
   */
  public String getNote() {
    if (note == null) {
      return "";
    }
    return note;
  }

  /**
   * Accède au numéro de téléphone
   *
   * @return number Le numéro de téléphone
   */
  public String getNumber() {
    return number;
  }

  public void init() {
    id = 0;
    number = "";
    note = "";
  }

  public void setId(int id) {
    this.id = id;
  }

  public JSONObject toJSON() {
    JSONObject phone = new JSONObject();

    if (!number.isEmpty()) {
      phone.put("number", number);

      if (id != 0) {
        phone.put("id", id);
      }

      if (!note.isEmpty()) {
        phone.put("note", note);
      }
    }

    return phone;
  }
}
