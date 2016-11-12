package handler;

import api.APIConnector;
import model.item.Copy;
import model.member.StudentParent;
import org.json.JSONObject;

/**
 * Permet de reprendre des resultset et de les transformer en objet et de
 * transféré des objets de type Copy à des requêtes de BD
 *
 * @author Jessy
 * @since 28/03/2016
 * @version 1.0
 */
public class CopyHandler {
  private Copy copy;

  /**
   * Constructeur par défaut
   */
  public CopyHandler() {
    this.copy = new Copy();
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

    data.put("id", id);
    data.put("price", price);

    req.put("function", "update");
    req.put("object", "copy");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  /**
   * Ajout d'un copy à la BD
   * @param copy L'copy à ajouter
   * @return Identifiant de l'copy ajouté
   */
  public Copy addCopy(Copy copy) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("item_id", copy.getItem().getId());
    data.put("price", copy.getPrice());
    data.put("member_no", copy.getMember().getNo());

    req.put("object", "copy");
    req.put("function", "insert");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null) {
      copy.setId(data.optInt("id", 0));
      JSONObject reservation = data.optJSONObject("reservation");

      if (reservation != null) {
        copy.setParent(new StudentParent(reservation));
      }
    }

    return copy.getId() != 0 ? copy : null;
  }

  /**
   * Supprimer un copy de la BD
   * @param id copy à supprimer
   * @return true si supprimé
   */
  public boolean deleteCopy(int id) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("id", id);

    req.put("object", "copy");
    req.put("function", "delete");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }
}