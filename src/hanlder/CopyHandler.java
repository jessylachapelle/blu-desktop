package hanlder;

import bd.AccesBD;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.article.Exemplaire;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Permet de reprendre des resultset et de les transformer en objet et de
 * transféré des objets de type Exemplaire à des requêtes de BD
 *
 * @author Jessy
 * @since 28/03/2016
 * @version 1.0
 */
public class CopyHandler {
  private Exemplaire exemplaire;

  /**
   * Constructeur par défaut
   */
  public void GestionnaireExemplaire() {
    this.exemplaire = new Exemplaire();
  }

  /**
   * Constructeur avec un exemplaire
   * @param exemplaire Exemplaire à utiliser
   */
  public void GestionnaireExemplaire(Exemplaire exemplaire) {
    this.exemplaire = exemplaire;
  }

  /**
   * Ajout d'un exemplaire à la BD
   * @param exemplaire L'exemplaire à ajouter
   * @return Identifiant de l'exemplaire ajouté
   */
  public int ajoutExemplaire(Exemplaire exemplaire) {
    int exemplaireId = 0;
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONObject response;

    try {
      data.append("id_article", exemplaire.getArticle().getNoArticle());
      data.append("prix", exemplaire.getPrix());
      data.append("noMembre", exemplaire.getMembre().getNoMembre());

      json.append("object", "exemplaire");
      json.append("function", "insert");
      json.append("data", data.toString());

      response = AccesBD.executeFunction(json);
      exemplaireId = response.getJSONObject("exemplaire").getInt("id");
    } catch (JSONException ex) {
      Logger.getLogger(CopyHandler.class.getName()).log(Level.SEVERE, null, ex);
    }

    return exemplaireId;
  }

  /**
   * Supprimer un exemplaire de la BD
   * @param exemplaire l'exemplaire à supprimer
   * @return true si supprimé
   */
  public boolean supprimeExemplaire(Exemplaire exemplaire) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.append("exemplaireId", exemplaire.getNoExemplaire());

      json.append("object", "exemplaire");
      json.append("function", "delete");
      json.append("data", data.toString());

      AccesBD.executeFunction(json);
      return true;
    } catch (JSONException ex) {
      Logger.getLogger(CopyHandler.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }
}