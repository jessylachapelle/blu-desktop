package model.member;

import java.util.ArrayList;
import model.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Un member de la BLU qui a le statut de parent-étudiant au Cégep, ce member a
 * le droit de réserver et d'obtenir 50% de rabais sur les achats
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
@SuppressWarnings("unused")
public class StudentParent extends Member {

  private ArrayList<Transaction> reservations;

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
  public ArrayList<Transaction> getReservations() {
    return reservations;
  }

  /**
   * Défini la liste des réservations
   *
   * @param reservations Une liste de réservation
   */
  public void setReservations(ArrayList<Transaction> reservations) {
    this.reservations = reservations;
  }

  /**
   * Ajout d'une réservation à la liste des réservations
   *
   * @param reservation La nouvelle réservation
   */
  public void addReservation(Transaction reservation) {
    this.reservations.add(reservation);
  }

  /**
   * Enlève une réservation qui a été annulé
   *
   * @param index L'indice de la position de la réservation
   */
  public void removeReservation(int index) {
    this.reservations.remove(index);
  }

  public void fromJSON(JSONObject parent) {
    super.fromJSON(parent);

    try {
      if (parent.has("reservations")) {
        JSONArray reservations = parent.getJSONArray("reservations");

        for (int i = 0; i < reservations.length(); i++) {
          this.reservations.add(new Transaction(reservations.getJSONObject(i)));
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
      for (Transaction reservation : this.reservations) {
        reservations.put(reservation.toJSON());
      }

      studentParent.put("reservation", reservations);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return studentParent;
  }
}
