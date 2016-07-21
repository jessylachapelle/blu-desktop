package handler;

import api.APIConnector;
import model.item.Copy;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permet de reprendre des resultset et de les transformer en objet et de
 * transféré des objets de type Copy à des requêtes de BD
 *
 * @author Jessy
 * @since 28/03/2016
 * @version 1.0
 */
@SuppressWarnings("unused")
public class CopyHandler {
  private Copy copy;

  /**
   * Constructeur par défaut
   */
  public CopyHandler() {
    this.copy = new Copy();
  }

  /**
   * Constructeur avec un copy
   * @param copy Copy à utiliser
   */
  public CopyHandler(Copy copy) {
    this.copy = copy;
  }

  public Copy getCopy() {
    return copy;
  }
  /**
   * Modifie un copy dans la base de données
   *
   * @param copyId Le numéro de l'copy
   * @param price Le nouveau prix
   * @return True si l'copy a été modifié
   */
  public boolean updateCopyPrice(int copyId, double price) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", copyId);
      data.put("price", price);

      json.put("function", "update");
      json.put("object", "copy");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Supprime un copy de la base de données
   *
   * @param copyId L'identifiant de l'exmplaire
   * @return Vrai si l'copy a été supprimé
   */
  public boolean deleteCopy(int copyId) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", copyId);
      json.put("function", "delete");
      json.put("object", "copy");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Ajout d'un copy à la BD
   * @param copy L'copy à ajouter
   * @return Identifiant de l'copy ajouté
   */
  public int addCopy(Copy copy) {
    int exemplaireId = 0;
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONObject response;

    try {
      data.put("item_id", copy.getItem().getId());
      data.put("price", copy.getPrice());
      data.put("member_no", copy.getMember().getNo());

      json.put("object", "copy");
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
   * Supprimer un copy de la BD
   * @param copy l'copy à supprimer
   * @return true si supprimé
   */
  public boolean deleteCopy(Copy copy) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.append("exemplaireId", copy.getId());

      json.append("object", "copy");
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