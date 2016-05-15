package model.transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Une transaction dans le système de la BLU, cela peut être l'ajout d'un
 * exemplaire, une vente, une remise d'argent, une réservation, etc.
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class Transaction {
  private int type,
              idExemplaire,
              noMembre;
  private Date date;

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
    type = 0;
    idExemplaire = 0;
    noMembre = 0;
    date = new Date();
  }

  /**
   * Constructeur qui crée une transaction avec toutes les informations
   *
   * @param type Le type de transaction
   * @param date La date de la transaction
   * @param idExemplaire L'exemplaire associé
   * @param noMembre Le membre associé
   */
  public Transaction(int type, Date date, int idExemplaire, int noMembre) {
    this.type = type;
    this.date = date;
    this.idExemplaire = idExemplaire;
    this.noMembre = noMembre;
  }

  /**
   * Accède au type de transaction
   *
   * @return type Le type de transaction
   */
  public int getType() {
    return type;
  }

  /**
   * Modifie le type de transaction
   *
   * @param type Le type de transaction
   */
  public void setType(int type) {
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

  public String getDateStr() {
    return (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
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
   * @param strDate Date au format aaaa-mm-jj
   */
  public void setDate(String strDate) {
    Date date = new Date();

    String annee = strDate.substring(0, 4);
    String mois = strDate.substring(5, 7);
    String jour = strDate.substring(8, 10);
    date.setYear(Integer.parseInt(annee) - 1900);
    date.setMonth(Integer.parseInt(mois) - 1);
    date.setDate(Integer.parseInt(jour));

    this.date = date;
  }

  /**
   * Accède au numéro de l'exemplaire associé à la transaction
   *
   * @return idExemplaire Le numéro de l'exemplaire
   */
  public int getIdExemplaire() {
    return idExemplaire;
  }

  /**
   * Modifie le numéro d'exemplaire associé à la transaction
   *
   * @param idExemplaire Le numéro d'exemplaire
   */
  public void setIdExemplaire(int idExemplaire) {
    this.idExemplaire = idExemplaire;
  }

  /**
   * Accède au numéro du membre associé à la transaction
   *
   * @return noMembre Le numéro du membre
   */
  public int getNoMembre() {
    return noMembre;
  }

  /**
   * Modifie le numéro du membre associé à la transaction
   *
   * @param noMembre Le numéro du membre
   */
  public void setNoMembre(int noMembre) {
    this.noMembre = noMembre;
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("type")) {
        type = json.getInt("type");
      }

      if (json.has("id_exemplaire")) {
        idExemplaire = json.getInt("id_exemplaire");
      }

      if (json.has("no_membre")) {
        noMembre = json.getInt("no_membre");
      }

      if (json.has("date")) {
        setDate(json.getString("date"));
      }
    } catch (JSONException e) {}
  }
}
