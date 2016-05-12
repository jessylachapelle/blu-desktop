package bd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import model.transaction.Transaction;
import ressources.JsonParser;

/**
 * Les requêtes SQL exécutées sur la BD qui concernent une transaction
 *
 * @author Jessy Lachapelle
 * @since 12/11/2015
 * @version 0.2
 */
public class TransactionSQL {

  /**
   * Requête d'une liste de transactions d'un membre
   *
   * @param noMembre Le numéro du membre
   * @return L'information des transactions associés par clé-valeur
   */
  static public ArrayList<HashMap> selectTransaction(int noMembre) {
    String query = "SELECT * FROM transaction WHERE no_membre=" + Integer.toString(noMembre);
    return executeQuery(query);
  }

  /**
   * Requête d'une liste de transactions d'un exemplaire d'un membre
   *
   * @param idExemplaire Le numéro de l'exemplaire
   * @return L'information des transactions associés par clé-valeur
   */
  static public ArrayList<HashMap> selectTransactionExemplaire(int idExemplaire) {
    String query = "SELECT * FROM transaction "
            + "WHERE id_exemplaire=" + Integer.toString(idExemplaire);
    return executeQuery(query);
  }

  /**
   * Requête d'une liste de transactions d'une date
   *
   * @param date La date ciblée
   * @return L'information des transactions associés par clé-valeur
   */
  static public ArrayList<HashMap> selectTransaction(Date date) {
    String query = "SELECT * FROM transaction WHERE date='" + date + "'";
    return executeQuery(query);
  }

  /**
   * Insère une transaction dans la BD
   *
   * @param transaction Une transaction
   * @return Vrai si l'insertion se complète
   */
  static public boolean insertTransaction(Transaction transaction) {
    String query = "INSERT INTO transaction(id_type, no_membre, id_exemplaire, date) "
            + "VALUES (" + transaction.getType() + ","
            + transaction.getNoMembre() + ","
            + transaction.getIdExemplaire() + ",'"
            + transaction.getDateStr() + "');";

    return executeUpdate(query);
  }

  static public boolean deleteTransactionsExemplaire(int idExemplaire) {
    String query = "DELETE FROM transaction WHERE id_exemplaire=" + Integer.toString(idExemplaire);
    return executeUpdate(query);
  }

  static public boolean deleteTransactionVente(int idExemplaire) {
    String query = "DELETE FROM transaction WHERE id_exemplaire=" + Integer.toString(idExemplaire) + " AND (id_type=2 OR id_type=3)";
    return executeUpdate(query);
  }

  /**
   * Exécute une requête SQL et retourne un résultat
   */
  static private ArrayList<HashMap> executeQuery(String query) {
    return JsonParser.toHashMap(AccesBD.executeQuery(query));
  }

  /**
   * Exécute une requête SQL et retourne vrai si elle est complétée
   */
  static private boolean executeUpdate(String query) {
    return AccesBD.executeUpdate(query);
  }
}
