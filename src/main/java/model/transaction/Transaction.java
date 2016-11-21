package model.transaction;

import java.util.Date;

import org.json.JSONObject;

import model.member.StudentParent;
import utility.DateParser;

/**
 * Une transaction dans le système de la BLU, cela peut être l'ajout d'un
 * exemplaire, une vente, une remise d'argent, une réservation, etc.
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class Transaction {
  public final static class Type {
    public final static String RESERVATION = "RESERVE";
    public final static String DONATION = "DONATE";
  }

  private Date date;
  private String type;
  private StudentParent parent;

  public Transaction(JSONObject json) {
    _init();
    fromJSON(json);
  }

  public Transaction(String type) {
    this.type = type;
    parent = new StudentParent();
    date = new Date();
  }

  /**
   * Accède à la date de la transaction
   *
   * @return date La date de la transaction
   */
  public Date getDate() {
    return date;
  }

  public void fromJSON(JSONObject transaction) {
    setDate(transaction.optString("date", _getDateString()));
    type = transaction.optString("code", type);

    JSONObject parent = transaction.optJSONObject("parent");
    if (parent != null) {
      this.parent = new StudentParent(parent);
    }
  }

  public StudentParent getParent() {
    return parent;
  }

  public String getType() {
    return type;
  }

  /**
   * Modifie la date de la transaction
   *
   * @param date La date de la transaction
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   *
   * @param dateString Formatted date (YYYY-MM-DD)
   */
  public void setDate(String dateString) {
    date = dateString.isEmpty() ? date : DateParser.dateFromString(dateString);
  }

  public void setParent(StudentParent parent) {
    this.parent = parent;
  }

  public JSONObject toJSON() {
    JSONObject transaction = new JSONObject();

    transaction.put("date", _getDateString());
    transaction.put("type", type);

    return transaction;
  }

  private String _getDateString() {
    return DateParser.dateToString(date);
  }

  private void _init() {
    type = "";
    parent = new StudentParent();
    date = new Date();
  }
}
