/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import model.report.Report;
import ressources.JsonParser;

/**
 * Contient les requêtes pour créer les rapports demandés
 *
 * @author Dereck
 */
public class RapportSQL {

  static private ArrayList<HashMap> executeQuery(String query) {
    return JsonParser.toHashMap(AccesBD.executeQuery(query));
  }

  static private ResultSet executeQueryRS(String query) {
    return (ResultSet) AccesBD.executeQuery(query);
  }

  /**
   * Exécute une requête SQL et retourne vrai si elle est complétée
   */
  static private boolean executeUpdate(String query) {
    return AccesBD.executeUpdate(query);
  }

  /**
   * Selectionne tous les membres actifs dans la BD
   *
   * @return HashMap contenant les résultats de la requête
   */
  static public ArrayList<HashMap> selectMembreActif() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    date.setYear(date.getYear() - 1);
    String datestring = dateFormat.format(date);

    String query = "SELECT no, nom, prenom, solde FROM membre WHERE derniere_activite>'" + datestring + "';";

    return executeQuery(query);
  }

  /**
   * Calcule le nombre de lignes retournées par une requête SQL
   *
   * @param query La requête SQL
   * @return Le nombre de lignes
   * @throws SQLException
   */
  static public int retourneNbRow(ResultSet rs) throws SQLException {
    int nbLignes = 0;

    rs.last();
    nbLignes = rs.getRow();
    rs.beforeFirst();

    return nbLignes;
  }

  static public int retourneValeurTotal(ResultSet rs) throws SQLException {
    int total = 0;

    while (rs.next()) {
      int idExemplaire = rs.getInt("id_exemplaire");

      String query2 = "SELECT prix FROM exemplaire WHERE id = " + idExemplaire + ";";
      HashMap hmPrix = executeQuery(query2).get(0);
      int prixExemplaire = (int) hmPrix.get("prix");

      total += prixExemplaire;
    }
    return total;
  }

  /**
   * Retourne le nombre d'objet mis en ventes et leur valeur totale
   *
   * @param dateDebut La date de debut du report
   * @param dateFin La date de fin du report
   * @return Le nombre de transaction de mise en vente
   * @throws SQLException
   */
  static public ArrayList<Integer> selectMiseEnVente(Date dateDebut, Date dateFin) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 1 AND WHERE date >'"
            + dateDebut + "' AND date < '" + dateFin + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);
    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

  /**
   * Retourne le nombre d'objets mis en vente et leur valeur totale
   *
   * @param report Le report associé
   * @return Le nombre de transaction de mise en vente
   * @throws SQLException
   */
  static public ArrayList<Integer> selectMiseEnVente(Report report) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 1 AND WHERE date >'"
            + report.getDateDebut() + "' AND date < '" + report.getDateFin() + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);
    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

  /**
   * Retourne le nombre de livre vendus et leur valeur totale pour une période
   * donnée
   *
   * @param dateDebut La date de debut du report
   * @param dateFin La date de fin du report
   * @return Le nombre de transaction de mise en vente
   * @throws SQLException
   */
  static public ArrayList<Integer> selectVente(Date dateDebut, Date dateFin) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 2 AND WHERE date >'"
            + dateDebut + "' AND date < '" + dateFin + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);

    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

  /**
   * Retourne le nombre d'objets vendu et leur valeur totale
   *
   * @param report Le report associé
   * @return Le nombre de transaction de vente
   * @throws SQLException
   */
  static public ArrayList<Integer> selectVente(Report report) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 1 AND WHERE date >'"
            + report.getDateDebut() + "' AND date < '" + report.getDateFin() + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);

    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

  /**
   * Calcule l'argent remis au membres et le nombre de livres remboursés
   *
   * @param dateDebut La date de debut du report
   * @param dateFin La date de fin du report
   * @return La somme de l'argent remis
   * @throws SQLException
   */
  static public ArrayList<Integer> selectRemiseArgent(Date dateDebut, Date dateFin) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 3 AND WHERE date >'"
            + dateDebut + "' AND date < '" + dateFin + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);

    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

  /**
   * Calcule l'argent remis au membres et le nombre de livres remboursés
   *
   * @param report Le report associé
   * @return La somme de l'argent remis
   * @throws SQLException
   */
  static public ArrayList<Integer> selectRemiseArgent(Report report) throws SQLException {

    String query = "SELECT * FROM transaction WHERE id_type = 3 AND WHERE date >'"
            + report.getDateDebut() + "' AND date < '" + report.getDateFin() + "';";

    ResultSet rs = executeQueryRS(query);

    int nbLivre = retourneNbRow(rs);
    int total = retourneValeurTotal(rs);

    ArrayList<Integer> info = new ArrayList<Integer>();

    info.add(nbLivre);
    info.add(total);

    return info;
  }

}
