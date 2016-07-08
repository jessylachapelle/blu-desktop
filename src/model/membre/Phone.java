package model.membre;

import org.json.JSONException;
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
    fromJson(json, true);
  }

  private void init() {
    id = 0;
    number = "";
    note = "";
  }

  /**
   * Constructeur d'un numéro de téléphone sans note
   *
   * @param number Numéro de téléphone
   */
  public Phone(String number) {
    setNumber(number);
    note = null;
  }

  /**
   * Constructeur d'un numéro de téléphone avec note
   *
   * @param number Numéro de téléphone
   * @param note Note associée au numéro
   */
  public Phone(String number, String note) {
    setNumber(number);
    setNote(note);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  /**
   * Accède au numéro de téléphone
   *
   * @return number Le numéro de téléphone
   */
  public String getNumber() {
    return number;
  }

  /**
   * Modifie le numéro de téléphone
   *
   * @param number Le nouveau numéro de téléphone
   */
  public void setNumber(String number) {
    this.number = number;
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
   * Modifie la note associée au numéro
   *
   * @param note Note associé au numéro
   */
  public void setNote(String note) {
    this.note = note;
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

  public void fromJson(JSONObject json) {
    fromJson(json, false);
  }

  public void fromJson(JSONObject json, boolean reinitialize) {
    if (reinitialize) {
      init();
    }

    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("number")) {
        number = json.getString("number");
      }

      if (json.has("note")) {
        note = json.getString("note");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
