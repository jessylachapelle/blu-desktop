package hanlder;

import static bd.TransactionSQL.*;
import model.transaction.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Permet de reprendre des resultset et de les transformer en objet et de
 * transféré des objets de type Transaction à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 12/11/2015
 * @version 0.2
 */
public class TransactionHalder {

  private static final int RESERVATION = 5;
  private Transaction transaction;
  private ArrayList<Transaction> transactions;

  /**
   * Constructeur par défaut
   */
  public TransactionHalder() {
    transactions = new ArrayList<>();
  }

  /**
   * Consulte la liste de transactions d'un membre
   *
   * @param noMembre Le numéro du membre
   * @return Une liste de transations
   */
  public ArrayList<Transaction> consulteTransactionMembre(int noMembre) {
    return construitTransaction(selectTransaction(noMembre));
  }

  /**
   * Consulte la liste de transactions d'une date
   *
   * @param date La date ciblée
   * @return Une liste de transations
   */
  public ArrayList<Transaction> consulteTransactionDate(Date date) {
    return construitTransaction(selectTransaction(date));
  }

  /**
   * Consulte la liste de transactions d'un exemplaie d'un membre
   *
   * @param idExemplaire Le numéro de l'exemplaire
   * @return Une liste de transactions
   */
  public ArrayList<Transaction> consulteTransactionExemplaire(int idExemplaire) {
    return construitTransaction(selectTransactionExemplaire(idExemplaire));
  }

  /**
   * Ajout d'une transaction dans la BD
   *
   * @param type Le type de transaction
   * @param noMembre Le numéro du membre
   * @param idExemplaire Le numéro de l'exemplaire
   * @return Vrai si l'ajout est complétée
   */
  public boolean ajoutTransaction(int type, int noMembre, int idExemplaire) {
    return insertTransaction(construitTransaction(type, noMembre, idExemplaire));
  }

  public boolean ajoutTransaction(int type, int idExemplaire) {
    return insertTransaction(construitTransaction(type, idExemplaire));
  }

  public boolean supprimeTransactionsExemplaire(int idExemplaire) {
    return deleteTransactionsExemplaire(idExemplaire);
  }

  public boolean supprimeTransactionVente(int idExemplaire) {
    return deleteTransactionVente(idExemplaire);
  }

  /**
   * Construit une transaction
   */
  private Transaction construitTransaction(int type, int noMembre, int idExemplaire) {
    Date today = new Date();

    if (type == RESERVATION) {
      transaction = new Reservation();
    } else {
      transaction = new Transaction();
      transaction.setType(type);
    }

    transaction.setNoMembre(noMembre);
    transaction.setIdExemplaire(idExemplaire);
    transaction.setDate(today);

    return transaction;
  }

  private Transaction construitTransaction(int type, int idExemplaire) {
    Date today = new Date();
    transaction = new Transaction();

    transaction.setType(type);
    transaction.setIdExemplaire(idExemplaire);
    transaction.setDate(today);

    return transaction;
  }

  /**
   * Construit une transaction
   */
  private ArrayList<Transaction> construitTransaction(ArrayList<HashMap> maps) {
    transactions.clear();
    Transaction t;

    for (int noMap = 0; noMap < maps.size(); noMap++) {
      if ((int) maps.get(noMap).get("id_type") == RESERVATION) {
        MemberHandler gm = new MemberHandler();
        t = new Reservation();
      } else {
        t = new Transaction();
        t.setType((int) maps.get(noMap).get("id_type"));
      }

      t.setNoMembre((int) maps.get(noMap).get("no_membre"));
      t.setIdExemplaire((int) maps.get(noMap).get("id_exemplaire"));
      t.setDate((Date) maps.get(noMap).get("date"));
      transactions.add(t);
    }

    return transactions;
  }
}
