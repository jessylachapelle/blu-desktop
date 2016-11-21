package model.member;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import model.item.Reservation;

/**
 * Un member de la BLU qui a le statut de parent-étudiant au Cégep, ce member a
 * le droit de réserver et d'obtenir 50% de rabais sur les achats
 *
 * @author Jessy Lachapelle
 * @since 2016/07/24
 * @version 1.0
 */
public class StudentParent extends Member {

  private ArrayList<Reservation> reservations;

  /**
   * Constructeur par défaut, crée un objet aux valeurs null
   */
  public StudentParent() {
    super();
    reservations = new ArrayList<>();
  }

  public StudentParent(JSONObject parent) {
    super.init();
    fromJSON(parent);
  }

  public void fromJSON(JSONObject parent) {
    super.fromJSON(parent);
    JSONArray reservations = parent.optJSONArray("reservations");

    if (reservations != null) {
      this.reservations.clear();
      for (int i = 0; i < reservations.length(); i++) {
        JSONObject r = reservations.optJSONObject(i);
        if (r != null) {
          this.reservations.add(new Reservation(r));
        }
      }
    }
  }

  public JSONObject toJSON() {
    JSONObject studentParent = super.toJSON();
    JSONArray reservations = new JSONArray();

    for (Reservation reservation : this.reservations) {
      reservations.put(reservation.toJSON());
    }

    studentParent.put("reservation", reservations);

    return studentParent;
  }
}
