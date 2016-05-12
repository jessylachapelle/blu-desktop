package bd;

import java.util.ArrayList;
import java.util.HashMap;
import ressources.JsonParser;
import model.article.Article;

/**
 * Contient les requêtes faites à la Base de données reliées à Article
 *
 * @author Dereck
 * @verison 0.2
 * @since 2015/11/03
 */
public class ArticleSQL {
  private final static String[][] PROPRIETES = {
    {"1", "type"},
    {"2", "auteur_1"},
    {"3", "auteur_2"},
    {"4", "auteur_3"},
    {"5", "auteur_4"},
    {"6", "auteur_5"},
    {"8", "matiere"},
    {"9", "editeur"},
    {"11", "annee"},
    {"12", "edition"},
    {"13", "ean13"},
    {"14", "date_ajout"},
    {"15", "date_desuet"},
    {"16", "date_retire"},
    {"17", "commentaire"},
    {"19", "description"}
  };
  private final static String REQUETE_VALEUR = "SELECT propriete_valeur.valeur " +
                                               "FROM article " +
                                               "INNER JOIN propriete_article ON article.id = propriete_article.id_article " +
                                               "INNER JOIN propriete_valeur ON propriete_article.id_propriete_valeur = propriete_valeur.id " +
                                               "INNER JOIN propriete ON propriete_valeur.id_propriete = propriete.id ";

  private final static String REQUETE_ID = "SELECT propriete_valeur.id " +
                                           "FROM propriete_valeur " +
                                           "WHERE propriete_valeur.valeur = '";

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

  /**
   * Sélectionne les informations sur un article via son numéro
   * @param idArticle Le numéro de l'article
   * @return Un hashmap contenant les informations de l'article
   */
  static public HashMap selectArticle(int idArticle) {

    String query = "SELECT (SELECT nom FROM article WHERE id='" + idArticle + "') AS nom," +
                          "(SELECT id FROM article WHERE id='" + idArticle + "') AS id," +
                          "(SELECT valeur FROM propriete_valeur " +
                           "WHERE id=(SELECT propriete_valeur.id_association FROM article " +
                                     "INNER JOIN propriete_article ON article.id=propriete_article.id_article " +
                                     "INNER JOIN propriete_valeur ON propriete_article.id_propriete_valeur=propriete_valeur.id " +
                                     "WHERE article.id=" + idArticle + " AND propriete_valeur.id_propriete=8 LIMIT 1)) AS categorie,";

    for(int noPropriete = 0; noPropriete < PROPRIETES.length; noPropriete++) {
      query +=  "(" + REQUETE_VALEUR + "WHERE article.id= " + idArticle + " AND propriete_valeur.id_propriete='" + PROPRIETES[noPropriete][0] + "') AS " + PROPRIETES[noPropriete][1];

      if(noPropriete != PROPRIETES.length - 1)
        query += ",";
    }

    return executeQuery(query).get(0);
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
   * Sélectionne les informations sur tous les articles contenant le même nom
   *
   * @param nom Nom à chercher
   * @return Un hashmap contenant les informations de l'article
   */
  static public ArrayList<HashMap> selectListeArticle(String nom) {
    String query = "SELECT id, nom FROM article WHERE nom LIKE '#" + nom + "#';";
    return executeQuery(query, "listearticle");
  }

  /**
   * Sélectionne tous les informations pour une liste d'article selon le nom et
   * l'état
   *
   * @param nom Le nom à chercher
   * @param etat L'état de l'article
   * @return Un hashmap contenant les informations de l'article
   */
  static public ArrayList<HashMap> selectListeArticle(String nom, boolean etat) {
    String query = "SELECT id, nom FROM article WHERE nom LIKE '#" + nom + "#'"; // AND etat = " + etat + ";";
    return executeQuery(query, "listearticle");
  }

  /**
   * Sélectionne le nom d'un article selon le numéro
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le nom de l'article
   */
  static public HashMap selectNom(int noArticle) {
    String query = "SELECT nom FROM article WHERE noArticle = " + noArticle + ";";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne le type d'article d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le type de l'article
   */
  static public HashMap selectTypeArticle(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom =  'type_article';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne le premier auteur d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant l'auteur
   */
  static public HashMap selectAuteur(int noArticle) {
    String query = REQUETE_VALEUR + " WHERE article.id = " + noArticle + "propriete.nom = 'auteur_1'";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne un auteur d'un article selon le numéro de l'auteur
   *
   * @param noArticle Le numéro de l'article
   * @param noAuteur Le numéro de l'auteur (1 à 5)
   * @return Un hashmap contenant le nom de l'auteur
   */
  static public HashMap selectAuteur(int noArticle, int noAuteur) {
    String query = REQUETE_VALEUR + " WHERE article.id = " + noArticle + "propriete.nom = 'auteur_'" + noAuteur + ";";
    return executeQuery(query).get(0);
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
   * Sélectionne tous les auteurs pour un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant les noms des auteurs
   */
  static public ArrayList<HashMap> selectTousAuteur(int noArticle) {
    String article_propriete = "WHERE article.id = " + noArticle + "propriete.nom = ";
    String query = REQUETE_VALEUR + article_propriete + "1" + article_propriete + "2" + article_propriete + "3"
            + article_propriete + "4" + article_propriete + "5;";

    return executeQuery(query);
  }



  /**
   * Sélectionne la catégorie d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la catégorie de l'article
   */
  static public HashMap selectCategorie(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'categorie';";
    return executeQuery(query).get(0);
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
   * Sélectionne la matière d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la catégorie de l'article
   */
  static public HashMap selectMatiere(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'matiere';";
    return executeQuery(query).get(0);
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
   * Sélectionne le nom de l'éditeur d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le nom de l'éditeur de l'article
   */
  static public HashMap selectEditeur(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'editeur_Nom';";
    ArrayList<HashMap> myReturn = executeQuery(query);
    if (!myReturn.isEmpty())
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
   * Sélectionne le titre d'un ouvrage
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le titre de l'ouvrage
   */
  static public HashMap selectOuvrageTitre(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_titre';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne la date de parution de l'ouvrage
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la date de parution de l'article
   */
  static public HashMap selectOuvrageParution(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_parution';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne le numero d'édition d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le numero d'édition de l'article
   */
  static public HashMap selectOuvrageNoEdition(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_no_edition';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne le code barre de l'article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le code barre de l'article
   */
  static public HashMap selectOuvrageEAN(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_ean13';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne la date d'ajout de l'ouvrage
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la date d'ajout de l'article
   */
  static public HashMap selectOuvrageDateAjout(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_date_ajout';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne la date à laquelle l'ouvrage à été mis désuet
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la date de désuetude de l'article
   */
  static public HashMap selectOuvrageDateDesuet(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_date_desuet';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne la date à laquelle l'ouvrage à été retiré
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant la date de retrait de l'article
   */
  static public HashMap selectOuvrageDateRetrait(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + "propriete.nom = 'ouvrage_date_retrait';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne le commentaire relié à l'article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le commentaire
   */
  static public HashMap selectCommentaire(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id = " + noArticle + " propriete.nom = 'commentaire';";
    return executeQuery(query).get(0);
  }

  /**
   * Sélectionne les caisses de rangement reliées à l'article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant le numéro de la caisse de rangement
   */
  static public ArrayList<HashMap> selectUniteRangement(int noArticle) {
    String query = REQUETE_VALEUR + "WHERE article.id=" + noArticle + " AND propriete_valeur.id_propriete=18;";
    return executeQuery(query);
  }

  // TODO terminer les unités de rangement (insert/delete/update)
  static public boolean deleteUniteRangement(int idArticle) {
    String query = "DELETE FROM propriete_valeur " +
                   "INNER JOIN propriete_article " +
                   "ON propriete_valeur.id=propriete_article.id_propriete_valeur" +
                   "WHERE propriete_valeur.id_propriete=18" +
                   "AND propriete_article.id_article=" + idArticle;
    return executeUpdate(query);
  }

  static public boolean insertUniteRangement(int idArticle, String[] uniteRangement) {
    for(int noUniteRangement = 0; noUniteRangement < uniteRangement.length; noUniteRangement++) {
      String query = "";
      executeInsert("");
      query = "";
      executeUpdate("");
    }
    return true;
  }

  /**
   * Sélectionne l'état d'un article
   *
   * @param noArticle Le numéro de l'article
   * @return Un hashmap contenant l'état de l'article
   */
  static public HashMap selectEtat(int noArticle) {
    String query = "SELECT etat FROM article where article.id = " + noArticle;
    return executeQuery(query).get(0);
  }

  static public String selectPropriete(int noArticle, int noPropriete) {
    String query = "SELECT valeur FROM propriete_valeur "
            + "WHERE id_propriete=" + noPropriete
            + " AND id IN (SELECT id_propriete_valeur "
            + "FROM propriete_article "
            + "WHERE id_article=" + noArticle + ")";
    return (String) executeQuery(query).get(0).get("valeur");
  }

  /**
   * Insère une nouvelle matière dans la base de données
   *
   * @param nom Nom de la matière
   * @return Vrai si la matière a été ajoutée
   */
  static public boolean insertMatiere(String nom) {
    String query = "INSERT INTO propriete_valeur(id_propriete, valeur)"
            + "VALUES (8," + nom + ");";

    return executeUpdate(query);
  }

  /**
   * Modifie une matière dans la base de données
   *
   * @param nouveauNom Le nouveau nom de la matière
   * @param ancienNom Le nom erroné de la matière
   * @return Vrai si le nom a été modifié
   */
  static public boolean updateMatiere(String nouveauNom, String ancienNom) {
    String query = "UPDATE propriete_valeur SET valeur = " + nouveauNom + " WHERE id_propriete = 8"
            + "AND valeur LIKE '#" + ancienNom + "#';";
    return executeUpdate(query);
  }

  /**
   * Supprime une matière de la base de données
   *
   * @param nom Nom de la matière à supprimée
   * @return Vrai si la matière a été supprimée
   */
  static public boolean removeMatiere(String nom) {
    String query = "DELETE FROM propriete_valeur WHERE id_propriete = 8"
            + "AND valeur LIKE '#" + nom + "#';";

    return executeUpdate(query);
  }


  /**
   * Insère une nouvelle catégorie dans la base de données
   *
   * @param nom Nom de la catégorie
   * @return Vrai si la catégorie a été ajoutée
   */
  static public boolean insertCategorie(String nom) {
    String query = "INSERT INTO propriete_valeur(id_propriete, valeur)"
            + "VALUES (7," + nom + ");";

    return executeUpdate(query);
  }

  /**
   * Modifie une catégorie dans la base de données
   *
   * @param nouveauNom Le nouveau nom de la catégorie
   * @param ancienNom Le nom erroné de la catégorie
   * @return Vrai si le nom a été modifié
   */
  static public boolean updateCategorie(String nouveauNom, String ancienNom) {
    String query = "UPDATE propriete_valeur SET valeur = " + nouveauNom + " WHERE id_propriete = 7"
            + "AND valeur LIKE '#" + ancienNom + "#';";
    return executeUpdate(query);
  }

  /**
   * Supprime une catégorie de la base de données
   *
   * @param nom Nom de la catégorie à supprimée
   * @return Vrai si la catégorie a été supprimée
   */
  static public boolean removeCategorie(String nom) {
    String query = "DELETE FROM propriete_valeur WHERE id_propriete = 7"
            + "AND valeur LIKE '#" + nom + "#';";

    return executeUpdate(query);
  }

  /**
   * Ajoute un article dans la BD
   *
   * @param article L'article sur lequel est basé l'ajout
   * @return Vrai si l'article à été ajouté
   */
  static public int insertArticle(Article article) {
    String query = "INSERT INTO article(nom, etat)"
            + "VALUES ('"+ article.getNom() + "',"
            + article.getEtat() + ");";
    return executeInsert(query);
  }

  /**
   * Supprime les propriétés d'un article et l'article de la BD
   *
   * @param noArticle Le numéro de l'article à retiré
   * @return Vrai si c'est réussi ou faux si il y a un erreur
   */
  static public boolean removeArticle(int noArticle) {
    boolean reussite0 = false, reussite1 = false;

    String query0 = "DELETE FROM propriete_article WHERE id_article = " + noArticle + ";";
    reussite0 = executeUpdate(query0);

    String query1 = "DELTE FROM exemplaire WHERE id_article = " + noArticle + ";";
    reussite1 = executeUpdate(query1);

    if (reussite0 == true && reussite1 == true) {
      String query = "DELETE FROM article WHERE id = " + noArticle + ";";
      return executeUpdate(query);
    } else {
      return false;
    }
  }

  /**
   * Met a un jour l'état d'un article
   *
   * @param noArticle L'article à mettre à jour
   * @param etat L'etat de l'article
   * @return Vrai si la requête s'est bien exécutée
   */
  static public boolean updateEtat(int noArticle, int etat) {
    String query = "UPDATE article SET etat = " + etat + " WHERE id = "
            + noArticle + ";";

    return executeUpdate(query);
  }

  /**
   * Insère un Exemplaire basé sur un article
   *
   * @param noArticle L'article sur lequel est basé l'exemplaire
   * @param prix Le prix de l'exemplaire
   * @return Vrai si l'exemplaire a été ajouté
   */
  static public boolean insertExemplaire(int noArticle, int prix) {
    String query = "INSERT INTO exemplaire (id_article, prix) "
            + "VALUES (" + noArticle + ", " + prix + ");";
    return executeUpdate(query);
  }

  /**
   * Met a un jour un exemplaire selon le prix de celui ci
   *
   * @param noExemplaire L'identifiant de l'exemplaire
   * @param prix Nouveau prix de l'exemplaire
   * @return Vrai si l'exemplaire a été mis a jour
   */
  static public boolean updateExemplaire(int noExemplaire, double prix) {
    String query = "UPDATE exemplaire SET prix=" + prix + " WHERE id=" + noExemplaire;
    return executeUpdate(query);
  }

  /**
   * Supprime un exemplaire de la base de données
   *
   * @param noExemplaire L'identifiant de l'exemplaire
   * @return Vrai si l'exemplaire a été supprimé
   */
  static public boolean removeExemplaire(int noExemplaire) {
    String query = "DELETE FROM exemplaire WHERE id=" + noExemplaire;
    return executeUpdate(query);
  }

  /**
   * Supprime tous les exemplaires lié a un article de la base de données
   *
   * @param noArticle L'article auquel sont liés les exemplaires
   * @return Vrai si les exemplaires ont été supprimés
   */
  static public boolean removeTousExemplaire(int noArticle) {
    String query = "DELETE FROM exemplaire WHERE id_article = "
            + noArticle + ";";
    return executeUpdate(query);
  }

  /**
   * Insère un auteur pour un article
   *
   * @param noArticle Le numero de l'Article
   * @param nomAuteur Le nom de l'auteur
   * @param noAuteur Le numéro de l'auteur pour l'article (1 a 5)
   * @return Vrai si on reussi a insérer un auteur sinon faux
   */
  static public boolean insertAuteur(int noArticle, String nomAuteur, int noAuteur) {
    String query = "INSERT INTO propriete_valeur(id_propriete, valeur) "
            + "VALUES (" + (noAuteur + 1) + ", " + nomAuteur + ");";

    boolean reussite = executeUpdate(query);

    if (reussite) {
      String query1 = "SELECT id FROM propriete_valeur WHERE id_propriete = "
              + (noAuteur + 1) + "AND valeur = " + nomAuteur + ";";
      HashMap id = executeQuery(query1).get(0);

      int id_propriete = (int) id.get(0);

      String query2 = "INSERT INTO propriete_valeur (id_propriete_valeur, id_article) "
              + "VALUES (" + id_propriete + ", " + noArticle + ";";

      return executeUpdate(query2);
    } else {
      return false;
    }

  }

  static public ArrayList<HashMap> selectExemplairesMembre(int noMembre, int etat) {
    String query = "SELECT exemplaire.id AS exemplaire_id, article.id AS article_id, article.nom AS titre, transaction.date AS date, exemplaire.prix AS prix FROM transaction "
            + "INNER JOIN exemplaire ON transaction.id_exemplaire=exemplaire.id "
            + "INNER JOIN article ON exemplaire.id_article=article.id "
            + "WHERE no_membre=" + noMembre;

    if (etat == 2 || etat == 3)
      query += " AND (id_type=2 OR id_type=3)";
    else
      query += " AND id_type=" + etat;

    if (etat == 1)
      query += " AND id_exemplaire NOT IN(SELECT id_exemplaire FROM transaction WHERE no_membre=" + noMembre + " AND (id_type=2 OR id_type=3))";
    if (etat == 2 || etat == 3)
      query += " AND id_exemplaire NOT IN(SELECT id_exemplaire FROM transaction WHERE no_membre=" + noMembre + " AND id_type=4)";

    return executeQuery(query);
  }

  static public ArrayList<HashMap> selectExemplairesArticle(int idArticle, int etat) {
    String query = "SELECT exemplaire.id, exemplaire.prix, membre.no, membre.prenom, membre.nom " +
                   "FROM exemplaire " +
                   "INNER JOIN transaction ON exemplaire.id=transaction.id_exemplaire " +
                   "INNER JOIN membre ON transaction.no_membre=membre.no " +
                   "WHERE exemplaire.id_article='" + idArticle + "'";

    if (etat == 2 || etat == 3)
      query += " AND (transaction.id_type=2 OR transaction.id_type=3)";
    else
      query += " AND transaction.id_type=" + etat;

    if (etat == 1)
      query += " AND transaction.id_exemplaire NOT IN(SELECT exemplaire.id FROM exemplaire INNER JOIN transaction ON exemplaire.id=transaction.id_exemplaire WHERE exemplaire.id_article=" + idArticle + " AND (transaction.id_type=2 OR transaction.id_type=3))";
    if (etat == 2 || etat == 3)
      query += " AND transaction.id_exemplaire NOT IN(SELECT exemplaire.id FROM exemplaire INNER JOIN transaction ON exemplaire.id=transaction.id_exemplaire WHERE exemplaire.id_article=" + idArticle + " AND transaction.id_type=4)";

    return executeQuery(query, "exemplaireliste");
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

  static public boolean insertCommentaire(int idArticle, String commentaire) {
    return insertPropriete(idArticle, 17, commentaire);
  }

  static public boolean updateCommentaire(int idArticle, String commentaire) {
    String query = "UPDATE propriete_valeur " +
                   "INNER JOIN propriete_article ON propriete_valeur.id=propriete_article.id_propriete_valeur " +
                   "SET propriete_valeur.valeur='" + commentaire + "' " +
                   "WHERE propriete_article.id_article=" + idArticle + " " +
                   "AND propriete_valeur.id_propriete=17";
    return executeUpdate(query);
  }

  static public boolean deleteCommentaire(int idArticle) {
    return deletePropriete(idArticle, 17);
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