package model.transaction;

import org.json.JSONException;
import org.json.JSONObject;
import ressources.DateParser;

import java.util.Date;

/**
 * Une transaction dans le système de la BLU, cela peut être l'ajout d'un
 * exemplaire, une vente, une remise d'argent, une réservation, etc.
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
@SuppressWarnings("unused")
public class Transaction {
  private int id,
              idCopy,
              noMember;
  private Date date;
  private String type;

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

  private void init() {
    id = 0;
    type = "";
    idCopy = 0;
    noMember = 0;
    type = "";
    date = new Date();
  }

  /**
   * Constructeur qui crée une transaction avec toutes les informations
   *
   * @param type Le type de transaction
   * @param date La date de la transaction
   * @param idCopy L'exemplaire associé
   * @param noMember Le member associé
   */
  public Transaction(String type, Date date, int idCopy, int noMember) {
    this.type = type;
    this.date = date;
    this.idCopy = idCopy;
    this.noMember = noMember;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  /**
   *
   * @param dateString Formatted date (YYYY-MM-DD)
   */
  public void setDate(String dateString) {
    this.date = DateParser.dateFromString(dateString);
  }

  /**
   * Accède au numéro de l'exemplaire associé à la transaction
   *
   * @return idCopy Le numéro de l'exemplaire
   */
  public int getIdCopy() {
    return idCopy;
  }

  /**
   * Modifie le numéro d'exemplaire associé à la transaction
   *
   * @param idCopy Le numéro d'exemplaire
   */
  public void setIdCopy(int idCopy) {
    this.idCopy = idCopy;
  }

  /**
   * Accède au numéro du member associé à la transaction
   *
   * @return noMember Le numéro du member
   */
  public int getNoMember() {
    return noMember;
  }

  /**
   * Modifie le numéro du member associé à la transaction
   *
   * @param noMember Le numéro du member
   */
  public void setNoMember(int noMember) {
    this.noMember = noMember;
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("date")) {
        setDate(json.getString("date"));
      }

      if (json.has("copy")) {
        idCopy = json.getInt("copy");
      }

      if (json.has("member")) {
        noMember = json.getInt("member");
      }

      if (json.has("code")) {
        type = json.getString("code");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject transaction = new JSONObject();

    try {
      transaction.put("id", id);
      transaction.put("copy", idCopy);
      transaction.put("member", noMember);
      transaction.put("date", getDateString());
      transaction.put("type", type);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return transaction;
  }
}
