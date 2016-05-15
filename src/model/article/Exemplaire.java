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
  private int noExemplaire;
  private Membre membre,
                 parent;
  private Article article;
  private double prix;
  private ArrayList<Transaction> transaction;

  public Exemplaire() {
    init();
  }

  public Exemplaire(JSONObject json) {
    init();
    fromJSON(json);
  }

  private void init() {
    noExemplaire = 0;
    membre = new Membre();
    parent = new ParentEtudiant();
    article = new Article();
    prix = 0;
    transaction = new ArrayList<>();
  }

  /**
   * Récupère la valeur du numéro d'exemplaire
   *
   * @return noExemplaire Le numéro de l'exemplaire
   */
  public int getNoExemplaire() {
    return noExemplaire;
  }

  /**
   * Attribue une valeur au numéro de l'exemplaire
   *
   * @param noExemplaire Le numéro de l'exemplaire
   */
  public void setNoExemplaire(int noExemplaire) {
    this.noExemplaire = noExemplaire;
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
   * Récupère le prix de l'exemplaire
   *
   * @return prix Prix de l'exemplaire
   */
  public double getPrix() {
    return prix;
  }

  /**
   * Récupère le prix dans un format string
   *
   * @return Le prix en string
   */
  public String getStrPrix() {
    if(prix == 0)
      return "";
    return (int) prix + " $";
  }

  /**
   * Attribue une valeur au prix de l'exemplaire
   *
   * @param prix Prix de l'exemplaire
   */
  public void setPrix(double prix) {
    this.prix = prix;
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
  public boolean estVendu() {
    return (getEtat() == 2 || getEtat() == 3);
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
  public boolean estReserve() {
    return getEtat() == 5;
  }

  public boolean estEnVente() {
    return getEtat() == 1;
  }

  public boolean estRemis() {
    return getEtat() == 4;
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

  public String getNom() {
    return article.getNom();
  }

  public String getEdition() {
    if (article instanceof Ouvrage) {
      Ouvrage o = (Ouvrage) article;
      return String.valueOf(o.getNoEdition());
    }
    return "";
  }

  public String getEditeur() {
    if (article instanceof Ouvrage) {
      Ouvrage o = (Ouvrage) article;
      return o.getEditeur();
    }
    return "";
  }

  public String getEtiquetteVente() {
    return "$$$";
  }

  public String getDateAjout() {
    return getDate(1);
  }

  public String getDateVente() {
    if(!getDate(2).isEmpty())
      return getDate(2);
    return getDate(3);
  }

  public String getDateRemise() {
    return getDate(4);
  }

  public String getDateReservation() {
    return getDate(5);
  }

  public String getVendeur() {
    return membre.getPrenom() + " " + membre.getNom();
  }

  public String getReservant() {
    return parent.getPrenom() + " " + parent.getNom();
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id")) {
        noExemplaire = json.getInt("id");
      }

      if (json.has("prix")) {
        prix = json.getDouble("prix");
      }

      if (json.has("transaction")) {
        JSONArray transactions = new JSONArray();
        json.getJSONObject("transaction").toJSONArray(transactions);

        for(int i = 0; i < transactions.length(); i++) {
          transaction.add(new Transaction(transactions.getJSONObject(i)));
        }
      }

      if (json.has("membre")) {
        membre.fromJSON(json.getJSONObject("membre"));
      }

      if (json.has("article")) {
        // article.fromJSON(json.getJSONObject("article"));
      }
    } catch (JSONException e) {}
  }
}
