package bd;

import java.util.ArrayList;
import java.util.HashMap;
import ressources.JsonParser;
import model.article.Article;

/**
 * Contient les requêtes faites à la Base de données reliées à Article
 *
 * @author Dereck
 * @version 0.2
 * @since 2015/11/03
 */
public class ArticleSQL {
  private final static String REQUETE_ID = "SELECT propriete_valeur.id FROM propriete_valeur WHERE propriete_valeur.valeur='";

  static private ArrayList<HashMap> executeQuery(String query) {
    return JsonParser.toHashMap(AccesBD.executeQuery(query));
  }

  static private ArrayList<HashMap> executeQuery(String query, String param) {
    return JsonParser.toHashMap(AccesBD.executeQuery(query, param));
  }

  /**
   * Exécute une requête SQL et retourne vrai si elle est complétée
   */
  static private boolean executeUpdate(String query) {
    return AccesBD.executeUpdate(query);
  }

  /**
   * Exécute une requête SQL et retourne le id de l'insertion
   */
  static private int executeInsert(String query) {
    return AccesBD.executeInsert(query);
  }

  static public int selectIdArticle(String ean13) {
    String query = "SELECT id_article FROM propriete_article " +
                   "INNER JOIN propriete_valeur " +
                   "ON propriete_article.id_propriete_valeur=propriete_valeur.id " +
                   "WHERE id_propriete=13 AND valeur='" + ean13 + "'";

    return Integer.parseInt((String)executeQuery(query).get(0).get("id_article"));
  }

  static public boolean articleExiste(String ean13) {
    String query = "SELECT id_article FROM propriete_article " +
                   "INNER JOIN propriete_valeur " +
                   "ON propriete_article.id_propriete_valeur=propriete_valeur.id " +
                   "WHERE id_propriete=13 AND valeur='" + ean13 + "'";

    return !executeQuery(query).isEmpty();
  }

  /**
   * Sélectionne un auteur d'un article selon son nom
   *
   * @param nomAuteur le nom
   * @param noAuteur le numéro d'auteur
   * @return Un hashmap contenant le nom de l'auteur
   */
  static public HashMap selectAuteur(String nomAuteur, int noAuteur) {
    String query = REQUETE_ID + "WHERE propriete_valeur.valeur = '" + nomAuteur + "' AND propriete.nom = 'auteur_" + noAuteur + "';";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

   /**
   * Retourne toute le matière dans la bd
   *
   * @return Un hashmap contenant la catégorie de l'article
   */
  static public ArrayList<HashMap> selectAllMatiere() {
    String query = "SELECT propriete_valeur.id, propriete_valeur.valeur "
                + "FROM propriete_valeur "
                + "WHERE propriete_valeur.id_propriete = 8;";
    return executeQuery(query);
  }

  /**
   * Sélectionne la matière d'un article selon un nom de matière
   *
   * @param matiere le nom de la matière
   * @return Un hashmap contenant la catégorie de l'article
   */
  static public HashMap selectMatiere(String matiere) {
    String query = REQUETE_ID + matiere + "' AND propriete_valeur.id_propriete = 8;";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if(!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

    /**
   * Sélectionne l'id de l'éditeur d'un article
   *
   * @param editeur le nom de l'éditeur
   * @return Un hashmap contenant le nom de l'éditeur de l'article
   */
  static public HashMap selectEditeur(String editeur) {
    String query = REQUETE_ID + editeur + "' AND propriete_valeur.id_propriete = 9;";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

  /**
   * Sélectionne l'id de l'annéee d'un article
   *
   * @param annee l'annee en question
   * @return Un hashmap contenant l'id  de l'article
   */
  static public HashMap selectAnnee(String annee) {
    String query = REQUETE_ID + annee + "' AND propriete_valeur.id_propriete = 11;";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

   /**
   * Sélecction l'id de l'édition d'un article
   *
   * @param edition l'édition en question.
   * @return Un hashmap contenant l'id  de l'article
   */
  static public HashMap selectEdition(String edition) {
    String query = REQUETE_ID + edition + "' AND propriete_valeur.id_propriete = 12;";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

    /**
   * Sélecction l'id du code bar d'un article
   *
   * @param ean13 le code bar en question.
   * @return Un hashmap contenant l'id  de l'article
   */
  static public HashMap selectEAN13(String ean13) {
    String query = REQUETE_ID + ean13 + "' AND propriete_valeur.id_propriete = 13;";
     ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

  /**
  * Sélecction l'id de la  d'un article
  *
  * @param date le code bar en question.
  * @return Un hashmap contenant l'id  de l'article
  */
  static public HashMap selectDateAjout(String date) {
    String query = REQUETE_ID + date + "' AND propriete_valeur.id_propriete = 14;";
     ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
      return myReturn.get(0);
    else
      return null;
  }

  /**
   * Ajoute un article dans la BD
   *
   * @param article L'article sur lequel est basé l'ajout
   * @return Vrai si l'article à été ajouté
   */
  static public int insertArticle(Article article) {
    String query = "INSERT INTO article(nom, etat)"
            + "VALUES ('"+ article.getName() + "',"
            + article.getEtat() + ");";
    return executeInsert(query);
  }

  static public ArrayList<HashMap> selectExemplairesArticle(int idArticle) {
    String query = "SELECT exemplaire.id, exemplaire.prix, membre.no, membre.prenom, membre.nom " +
                   "FROM exemplaire " +
                   "INNER JOIN transaction ON exemplaire.id=transaction.id_exemplaire " +
                   "INNER JOIN membre ON transaction.no_membre=membre.no " +
                   "WHERE exemplaire.id_article='" + idArticle + "' " +
                   "AND transaction.id_type=1";

    return executeQuery(query, "exemplaireliste");
  }

  static public ArrayList<HashMap> selectDemandesReservation(int idArticle) {
    String query = "SELECT membre.no, membre.prenom, membre.nom, reservation.date " +
                   "FROM reservation " +
                   "INNER JOIN membre ON reservation.no_membre=membre.no " +
                   "WHERE id_article=" + idArticle;

    return executeQuery(query);
  }

  static public boolean deleteDemandeReservation(int noMembre, int idArticle) {
    String query = "DELETE FROM reservation WHERE no_membre=" + noMembre + " AND id_article=" + idArticle;
    return executeUpdate(query);
  }

  static public boolean insertDemandeReservation(int noMembre, int idArticle) {
    String query = "INSERT INTO reservation(no_membre, id_article) VALUES ("+ noMembre + "," + idArticle + ")";
    return executeUpdate(query);
  }

  static public boolean deleteReservation(int idExemplaire) {
    String query = "DELETE FROM transaction WHERE id_type=5 AND id_exemplaire=" + idExemplaire;
    return executeUpdate(query);
  }

  static public boolean insertDateDesuet(int idArticle, String date) {
    return insertPropriete(idArticle, 15, date);
  }

  static public boolean deleteDateDesuet(int idArticle) {
    return deletePropriete(idArticle, 15);
  }

  static public boolean insertDateRetire(int idArticle, String date) {
    return insertPropriete(idArticle, 16, date);
  }

  static public boolean deleteDateRetire(int idArticle) {
    return deletePropriete(idArticle, 16);
  }

  static private boolean deletePropriete(int idArticle, int idPropriete) {
    String query = "SELECT propriete_valeur.id FROM propriete_valeur " +
                   "INNER JOIN propriete_article ON propriete_valeur.id=propriete_article.id_propriete_valeur " +
                   "WHERE propriete_article.id_article=" + idArticle + " " +
                   "AND propriete_valeur.id_propriete=" + idPropriete;

    int idProprieteValeur = Integer.parseInt((String)executeQuery(query).get(0).get("id"));

    query = "DELETE FROM propriete_article WHERE id_article=" + idArticle + " AND id_propriete_valeur=" + idProprieteValeur;

    if(executeUpdate(query)) {
      query = "DELETE FROM propriete_valeur WHERE id=" + idProprieteValeur;
      return executeUpdate(query);
    } return false;
  }

  static public boolean insertPropriete(int idArticle, int idPropriete, String valeur) {
    String query = "INSERT INTO propriete_valeur(id_propriete, valeur) VALUES (" + idPropriete + ",'" + valeur + "')";
    query = "INSERT INTO propriete_article(id_propriete_valeur, id_article) VALUES (" + executeInsert(query) + "," + idArticle + ")";
    return executeUpdate(query);
  }

  /**
   * insert l'association entre une valeur de propriété donnée et un article
   * @param idArticle l'article
   * @param idProprieteValeur l'id de la valeur
   * @return le resultat de l'opération
   */
  static public boolean insertProprieteArticle(int idArticle, int idProprieteValeur) {
    String query = "INSERT INTO propriete_article(id_propriete, id_article) VALUES (" + idProprieteValeur + ",'" + idArticle + "')";
    return executeUpdate(query);
  }
}