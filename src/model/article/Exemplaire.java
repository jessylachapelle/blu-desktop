package model.article;

import model.transaction.Transaction;
import java.util.ArrayList;
import java.util.Date;

import model.membre.Membre;
import model.membre.ParentEtudiant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe exemplaire qui contient les informations spécifiques sur un
 * exemplaire. Un exemplaire est basé sur les informations générales d'un
 * article.
 *
 * @author Dereck
 * @date 30/10/2015
 */
public class Exemplaire {
  private int id;
  private Membre membre,
                 parent;
  private Article article;
  private double price;
  private ArrayList<Transaction> transaction;

  public Exemplaire() {
    init();
  }

  public Exemplaire(JSONObject json) {
    init();
    fromJSON(json);
  }

  private void init() {
    id = 0;
    membre = new Membre();
    parent = new ParentEtudiant();
    article = new Article();
    price = 0;
    transaction = new ArrayList<>();
  }

  /**
   * Récupère la valeur du numéro d'exemplaire
   *
   * @return id Le numéro de l'exemplaire
   */
  public int getId() {
    return id;
  }

  /**
   * Attribue une valeur au numéro de l'exemplaire
   *
   * @param id Le numéro de l'exemplaire
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Récupère l'article qui est lié à l'exemplaire
   *
   * @return article L'objet article avec les caractéristiques
   */
  public Article getArticle() {
    return article;
  }

  /**
   * Attribue un article à l'exemplaire
   *
   * @param article Un objet article avec les caractéristiques
   */
  public void setArticle(Article article) {
    this.article = article;
  }

  public Membre getMembre() {
    return membre;
  }

  public void setMembre(Membre membre) {
    this.membre = membre;
  }

  public ParentEtudiant getParent() {
    return (ParentEtudiant)parent;
  }

  public void setParent(Membre parent) {
    this.parent = (ParentEtudiant) parent;
  }

  /**
   * Récupère le price de l'exemplaire
   *
   * @return price Prix de l'exemplaire
   */
  public double getPrice() {
    return price;
  }

  /**
   * Récupère le price dans un format string
   *
   * @return Le price en string
   */
  public String getStrPrix() {
    if(price == 0)
      return "";
    return (int) price + " $";
  }

  /**
   * Attribue une valeur au price de l'exemplaire
   *
   * @param price Prix de l'exemplaire
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Récupère l'état de l'exemplaire
   *
   * @return 1 en vente, 2-3 vendu, 4 argent remis et 5 réservé
   */
  public int getEtat() {
    int etat = 1;

    for(int noTransaction = 0; noTransaction < transaction.size(); noTransaction++) {
      if(transaction.get(noTransaction).getType() > etat)
        etat = transaction.get(noTransaction).getType();
    }

    return etat;
  }

  public String getState() {
    boolean sold = false,
            payed = false;

    for(int i = 0; i < transaction.size(); i++) {
      if (transaction.get(i).getTypeCode().equals("SELL") || transaction.get(i).getTypeCode().equals("SELL_PARENT")) {
        sold = true;
      }

      if (transaction.get(i).getTypeCode().equals("PAY")) {
        payed = true;
      }
    }

    if (payed) {
      return "PAYED";
    }

    if (sold) {
      return "SOLD";
    }

    return "ADDED";
  }

  /**
   * Ajoute une transaction à l'exemplaire
   *
   * @param transaction Une transaction
   */
  public void ajouterTransaction(Transaction transaction) {
    this.transaction.add(transaction);
  }

  /**
   * Ajoute un array de transaction à l'exemplaire
   *
   * @param transactions Les transactions à ajoutées
   */
  public void ajouterTransactions(ArrayList<Transaction> transactions) {
    for (int nbTransaction = 0; nbTransaction < transactions.size(); nbTransaction++) {
      transaction.add(transaction.get(nbTransaction));
    }
  }

  /**
   * Récupère une transaction liée à un exemplaire
   *
   * @param index L'index de la transaction
   * @return transaction La transaction située à l'index
   */
  public Transaction getTransaction(int index) {
    return transaction.get(index);
  }

  /**
   * Récupère toutes les transactions liées à l'exemplaire
   *
   * @return transaction Une liste des transactions
   */
  public ArrayList<Transaction> getTousTransactions() {
    return transaction;
  }

  /**
   * Supprime une transaction liée à un exemplaire
   *
   * @param index L'index de la transaction
   */
  public void supprimerTransaction(int index) {
    transaction.remove(index);
  }

  /**
   * Supprime toutes les transactions liées à un exemplaire
   */
  public void supprimerTousTransactions() {
    transaction.clear();
  }

  /**
   *
   * @return Vrai si l'Exemplaire l'exemplaire est vendu
   */
  public boolean isSold() {
    return getState().equals("SOLD");
  }

  public boolean estVenduReg() {
    return getEtat() == 2;
  }

  public boolean estVenduRabais() {
    return getEtat() == 3;
  }

  /**
   *
   * @return Vrai si l'exemplaire est réservé
   */
  public boolean isReserved() {
    for(int i = 0; i < transaction.size(); i++) {
      if (transaction.get(i).getTypeCode().equals("RESERVE")) {
        return true;
      }
    }

    return false;
  }

  public boolean isAdded() {
    return getState().equals("ADDED");
  }

  public boolean isPayed() {
    return getState().equals("PAYED");
  }

  public String getDate(int type) {
    for (int noTransaction = 0; noTransaction < transaction.size(); noTransaction++) {
      if (transaction.get(noTransaction).getType() == type) {
        Date date = new Date();
        date = transaction.get(noTransaction).getDate();
        return date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
      }
    }
    return "";
  }

  private String getDate(String type) {
    for (int i = 0; i < transaction.size(); i++) {
      if (transaction.get(i).getTypeCode().equals(type)) {
        Date date = transaction.get(i).getDate();
        return date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
      }
    }
    return "";
  }

  public String getDateAdded() {
    return getDate("ADD");
  }

  public String getDateSold() {
    String date = getDate("SELL");

    if (date.isEmpty()) {
      return getDate("SELL_PARENT");
    }

    return date;
  }

  public String getDatePaid() {
    return getDate("PAY");
  }

  public String getName() {
    return article.getName();
  }

  public String getEdition() {
    if (article instanceof Ouvrage) {
      int edition = ((Ouvrage) article).getEdition();

      if (edition == 0) {
        return "";
      }

      return Integer.toString(edition);
    }
    return "";
  }

  public String getEditor() {
    if (article instanceof Ouvrage) {
      return ((Ouvrage) article).getEditor();
    }
    return "";
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("price")) {
        price = json.getDouble("price");
      }

      if (json.has("member")) {
        membre.fromJSON(json.getJSONObject("member"));
      }

      if (json.has("item")) {
        JSONObject itemData = json.getJSONObject("item");

        if (itemData.has("is_book") && itemData.getBoolean("is_book")) {
          article = new Ouvrage(itemData);
        } else {
          article = new Objet();
          ((Objet) article).fromJSON(itemData);
        }
      }

      if (json.has("transaction")) {
        JSONArray transactions = json.getJSONArray("transaction");

        for(int i = 0; i < transactions.length(); i++) {
          transaction.add(new Transaction(transactions.getJSONObject(i)));
        }
      }

      // TODO: DELETE
      if (json.has("prix")) {
        price = json.getDouble("prix");
      }

      if (json.has("membre")) {
        membre.fromJSON(json.getJSONObject("membre"));
      }

      if (json.has("article")) {
        article.fromJSON(json.getJSONObject("article"));
      }
    } catch (JSONException e) {}
  }
}
