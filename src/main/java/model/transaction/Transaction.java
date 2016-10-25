package model.transaction;

import java.util.Date;

import model.member.StudentParent;
import org.json.JSONException;
import org.json.JSONObject;

import utility.DateParser;

/**
 * Une transaction dans le système de la BLU, cela peut être l'ajout d'un
 * exemplaire, une vente, une remise d'argent, une réservation, etc.
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Transaction {
  private Date date;
  private String type;
  private StudentParent parent;

  /**
   * Constructeur par défaut, crée une transaction aux valeurs null
   */
  public Transaction() {
    init();
  }

  public Transaction(JSONObject json) {
    init();
    fromJSON(json);
  }

  public Transaction(String type) {
    this.type = type;
    parent = new StudentParent();
    date = new Date();
  }

  private void init() {
    type = "";
    parent = new StudentParent();
    date = new Date();
  }

  /**
   * Constructeur qui crée une transaction avec toutes les informations
   *
   * @param type Le type de transaction
   * @param date La date de la transaction
   */
  public Transaction(String type, Date date) {
    this.type = type;
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Accède à la date de la transaction
   *
   * @return date La date de la transaction
   */
  public Date getDate() {
    return date;
  }

  public String getDateString() {
    return DateParser.dateToString(date);
  }

  /**
   * Modifie la date de la transaction
   *
   * @param date La date de la transaction
   */
  public void setDate(Date date) {
    this.date = date;
  }

  public void setParent(StudentParent parent) {
    this.parent = parent;
  }

  public StudentParent getParent() {
    return parent;
  }

  /**
   *
   * @param dateString Formatted date (YYYY-MM-DD)
   */
  public void setDate(String dateString) {
    date = dateString.isEmpty() ? date : DateParser.dateFromString(dateString);
  }

  public void fromJSON(JSONObject transaction) {
    setDate(transaction.optString("date", ""));
    type = transaction.optString("code", "");

    JSONObject parent = transaction.optJSONObject("parent");
    if (parent != null) {
      this.parent = new StudentParent(parent);
    }
  }

  public JSONObject toJSON() {
    JSONObject transaction = new JSONObject();

    try {
      transaction.put("date", getDateString());
      transaction.put("type", type);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return transaction;
  }
}
