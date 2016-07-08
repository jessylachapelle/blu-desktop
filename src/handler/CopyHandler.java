package handler;

import api.APIConnector;

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
  public CopyHandler() {
    this.exemplaire = new Exemplaire();
  }



  /**
   * Constructeur avec un exemplaire
   * @param exemplaire Exemplaire à utiliser
   */
  public CopyHandler(Exemplaire exemplaire) {
    this.exemplaire = exemplaire;
  }

  /**
   * Modifie un exemplaire dans la base de données
   *
   * @param copyId Le numéro de l'exemplaire
   * @param price Le nouveau prix
   * @return True si l'exemplaire a été modifié
   */
  public boolean updateCopyPrice(int copyId, double price) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", copyId);
      data.put("price", price);

      json.put("function", "update");
      json.put("object", "exemplaire");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Supprime un exemplaire de la base de données
   *
   * @param copyId L'identifiant de l'exmplaire
   * @return Vrai si l'exemplaire a été supprimé
   */
  public boolean deleteCopy(int copyId) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", copyId);
      json.put("function", "delete");
      json.put("object", "exemplaire");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Ajout d'un exemplaire à la BD
   * @param exemplaire L'exemplaire à ajouter
   * @return Identifiant de l'exemplaire ajouté
   */
  public int addCopy(Exemplaire exemplaire) {
    int exemplaireId = 0;
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONObject response;

    try {
      data.put("item_id", exemplaire.getArticle().getId());
      data.put("price", exemplaire.getPrice());
      data.put("member_no", exemplaire.getMembre().getNo());

      json.put("object", "exemplaire");
      json.put("function", "insert");
      json.put("data", data);

      response = APIConnector.call(json).getJSONObject("data");
      exemplaireId = response.getJSONObject("copy").getInt("id");
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
  public boolean deleteCopy(Exemplaire exemplaire) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.append("exemplaireId", exemplaire.getId());

      json.append("object", "exemplaire");
      json.append("function", "delete");
      json.append("data", data.toString());

      APIConnector.call(json);
      return true;
    } catch (JSONException ex) {
      Logger.getLogger(CopyHandler.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }
}