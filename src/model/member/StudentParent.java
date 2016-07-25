package model.member;

import java.util.ArrayList;

import model.item.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Un member de la BLU qui a le statut de parent-étudiant au Cégep, ce member a
 * le droit de réserver et d'obtenir 50% de rabais sur les achats
 *
 * @author Jessy Lachapelle
 * @since 2016/07/24
 * @version 1.0
 */
@SuppressWarnings("unused")
public class StudentParent extends Member {

  private ArrayList<Reservation> reservations;

  /**
   * Constructeur par défaut, crée un objet aux valeurs null
   */
  public StudentParent() {
    super();
    reservations = new ArrayList<>();
  }

  /**
   * Accède à la liste de toutes les réservations du member
   *
   * @return reservations La liste des réservations
   */
  public ArrayList<Reservation> getReservations() {
    return reservations;
  }

  /**
   * Défini la liste des réservations
   *
   * @param reservations Une liste de réservation
   */
  public void setReservations(ArrayList<Reservation> reservations) {
    this.reservations = reservations;
  }

  /**
   * Ajout d'une réservation à la liste des réservations
   *
   * @param reservation La nouvelle réservation
   */
  public void addReservation(Reservation reservation) {
    this.reservations.add(reservation);
  }

  /**
   * Enlève une réservation qui a été annulé
   *
   * @param itemId L'indice de la position de la réservation
   */
  public void removeItemReservation(int itemId) {
    for (int i = 0; i < reservations.size(); i++) {
      if (reservations.get(i).getItem().getId() == itemId) {
        reservations.remove(i);
        break;
      }
    }
  }

  public void removeCopyReservation(int copyId) {
    for (int i = 0; i < reservations.size(); i++) {
      if (reservations.get(i).getCopy().getId() == copyId) {
        reservations.remove(i);
        break;
      }
    }
  }

  public void fromJSON(JSONObject parent) {
    super.fromJSON(parent);

    try {
      if (parent.has("reservations")) {
        JSONArray reservations = parent.getJSONArray("reservations");

        for (int i = 0; i < reservations.length(); i++) {
          this.reservations.add(new Reservation(reservations.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject studentParent = super.toJSON();
    JSONArray reservations = new JSONArray();

    try {
      for (Reservation reservation : this.reservations) {
        reservations.put(reservation.toJSON());
      }

      studentParent.put("reservation", reservations);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return studentParent;
  }
}
