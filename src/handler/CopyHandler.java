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
   * @param id Le numéro de l'copy
   * @param price Le nouveau prix
   * @return True si l'copy a été modifié
   */
  public boolean updateCopyPrice(int id, double price) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("price", price);

      req.put("function", "update");
      req.put("object", "copy");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      return data.has("code") && data.getInt("code") == 200;
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
  public Copy addCopy(Copy copy) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("item_id", copy.getItem().getId());
      data.put("price", copy.getPrice());
      data.put("member_no", copy.getMember().getNo());

      req.put("object", "copy");
      req.put("function", "insert");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");

      if (data.has("id")) {
        copy.setId(data.getInt("id"));
        return copy;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Supprimer un copy de la BD
   * @param id copy à supprimer
   * @return true si supprimé
   */
  public boolean deleteCopy(int id) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);

      req.put("object", "copy");
      req.put("function", "delete");
      req.put("data", data);

      JSONObject res = APIConnector.call(req);
      data = res.getJSONObject("data");
      return data.has("code") && data.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }
}