package bd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import model.membre.Membre;
import model.membre.ParentEtudiant;
import ressources.JsonParser;

/**
 * Les requêtes SQL exécutées sur la BD qui concernent un membre
 *
 * @author Jessy Lachapelle
 * @since 29/10/2015
 * @version 0.3
 */
public class MembreSQL {
  /**
   * Met à jour un membre dans la BD
   *
   * @param noMembre Le numéro du membre à mettre à jour
   * @param infoMembre Une liste d'association du champs à mettre à jour et de
   * sa nouvelle valeur
   * @return vrai si la mise à jour est complétée
   */
  static public boolean updateMembre(int noMembre, HashMap infoMembre) {
    Object[] champs = infoMembre.keySet().toArray();

    String set = "",
            query;

    for (int noChamps = 0; noChamps < champs.length; noChamps++) {
      String champ = champs[noChamps].toString();
      set += champ + "='" + (String) infoMembre.get(champ) + "'";

      if (noChamps + 1 != champs.length) {
        set += ", ";
      }
    }

    query = "UPDATE membre SET " + set
            + " WHERE no=" + Integer.toString(noMembre);

    return executeUpdate(query);
  }

  /**
   * Insère un nouveau membre dans la BD
   *
   * @param membre Le membre à insérer
   * @return vrai si l'insertion est complétée
   */
  static public boolean insertMembre(Membre membre) {
    int estParent = 0;
    int idVille = selectIdVille(membre.getVille());

    if (idVille == 0) {
      idVille = insertVille(membre);
    }

    if (ParentEtudiant.class.isInstance(membre)) {
      estParent = 1;
    }

    String query = "INSERT INTO membre(no, prenom, nom, courriel, parent, no_civic, rue, app, code_postal, id_ville) "
            + "VALUES (" + Integer.toString(membre.getNoMembre()) + ","
            + "'" + membre.getPrenom() + "',"
            + "'" + membre.getNom() + "',"
            + "'" + membre.getCourriel() + "',"
            + Integer.toString(estParent) + ","
            + Integer.toString(membre.getNoCivic()) + ","
            + "'" + membre.getRue() + "',"
            + "'" + String.valueOf(membre.getAppartement()) + "',"
            + "'" + String.valueOf(membre.getCodePostal()) + "',"
            + Integer.toString(idVille) + ");";
    if (executeUpdate(query)) {
      if (insertTelephone(membre, 0));
    }
    return insertTelephone(membre, 1);
  }

  private static int insertVille(Membre membre) {
    int idVille;
    // TODO coder la selction de province (HARDCODER A QUEBEC)
    String query = "INSERT INTO ville(nom, code_province)"
            + "VALUES (" + membre.getVille() + ","
            + "'QC')";
    executeUpdate(query);
    idVille = selectIdVille(membre.getVille());
    return idVille;
  }

  /**
   * Sélectionne les numéros de téléphone associés à un membre dans la BD
   *
   * @param noMembre Le numéro du membre
   * @return Le résultat de la requête
   */
  static public boolean insertTelephone(Membre membre, int indice) {
    String query = "INSERT INTO telephone(no_membre,numero,note)"
            + "VALUES (" + membre.getNoMembre() + ","
            + membre.getTelephone(indice).getNumero() + ","
            + membre.getTelephone(indice).getNote() + ");";
    return executeUpdate(query);
  }

  /**
   * Supprime un membre de la BD
   *
   * @param noMembre Le numéro du membre à supprimer
   * @return vrai si la suppresion est complétée
   */
  static public boolean deleteMembre(int noMembre) {
    deleteCommentaireMembre(noMembre);
    String query = "DELETE * FROM membre WHERE no" + Integer.toString(noMembre);
    return executeUpdate(query);
  }

  static private boolean deleteCommentaireMembre(int noMembre) {
    String query = "DELETE * FROM commentaire WHERE no_membre" + Integer.toString(noMembre);
    return executeUpdate(query);
  }

  /**
   * Transfère les transactions et le solde d'un membre vers un autre [NOTE :
   * POUR LES DOUBLONS UNIQUEMENT]
   *
   * @param noMembreBon Le numéro du bon membre
   * @param noMembreMauvais Le numéro du membre doublon
   * @return vrai si le transfère est complété
   */
  static public boolean transferMembre(int noMembreBon, int noMembreMauvais) {
    HashMap infoMembre = new HashMap();
    boolean success;

    double solde = selectSolde(noMembreBon) + selectSolde(noMembreMauvais);
    infoMembre.put("solde", solde);

    String queryTransaction = "UPDATE transaction SET no='" + Integer.toString(noMembreBon)
            + "' WHERE no='" + Integer.toString(noMembreMauvais) + "'";

    success = executeUpdate(queryTransaction);

    if (success) {
      success = updateMembre(noMembreBon, infoMembre);
    }
    if (success) {
      success = deleteMembre(noMembreMauvais);
    }
    return success;
  }

  public static int insertCommentaire(int noMembre, String commentaire) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String datestring = dateFormat.format(date);

    String query = "INSERT INTO commentaire_membre(commentaire, no_membre, date_modification) "
            + "VALUES ('" + commentaire + "', "
            + Integer.toString(noMembre) + " , '"
            + datestring + "')";

    return executeInsert(query);
  }

  public static boolean updateCommentaire(int idCommentaire, String commentaire) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String datestring = dateFormat.format(date);

    String query = "UPDATE commentaire_membre "
            + "SET commentaire='" + commentaire + "', "
            + "date_modification='" + datestring + "' "
            + "WHERE id=" + Integer.toString(idCommentaire);

    return executeUpdate(query);
  }

  public static boolean deleteCommentaire(int idCommentaire) {
    String query = "DELETE FROM commentaire_membre WHERE id=" + Integer.toString(idCommentaire);
    return executeUpdate(query);
  }

  public static boolean membreExiste(int noMembre) {
    String query = "SELECT no FROM membre WHERE no=" + Integer.toString(noMembre);

    ArrayList<HashMap> maps = executeQuery(query);

    if(maps.isEmpty())
      return false;
    return maps.get(0).containsKey("no");
  }

  /**
   * Accède à l'identifiant d'une ville de la BD
   *
   * @param ville Le nom de la ville
   * @return id de la ville
   */
  static private int selectIdVille(String ville) {
    String query = "SELECT id FROM ville WHERE nom='" + ville + "'";
    ArrayList<HashMap> mapList = executeQuery(query);
    if (mapList.isEmpty()) {
      return 0;
    } else {
      HashMap<String, String> map = mapList.get(0);
      return Integer.valueOf(map.get("id"));
    }
  }

  /**
   * Accède au solde d'un membre dans la BD
   *
   * @param noMembre Le numéro du membre
   * @return Le solde du membre
   */
  static private double selectSolde(int noMembre) {
    String query = "SELECT solde FROM membre WHERE no=" + Integer.toString(noMembre);
    HashMap map = executeQuery(query).get(0);

    return (double) map.get("solde");
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

  /**
   * Exécute une requête SQL et retourne vrai si elle est complétée
   */
  static private int executeInsert(String query) {
    return AccesBD.executeInsert(query);
  }
}
