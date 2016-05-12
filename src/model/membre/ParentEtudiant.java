package model.membre;

import java.util.ArrayList;
import model.transaction.Reservation;

/**
 * Un membre de la BLU qui a le statut de parent-étudiant au Cégep, ce membre a
 * le droit de réserver et d'obtenir 50% de rabais sur les achats
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class ParentEtudiant extends Membre {

  private ArrayList<Reservation> reservations;

  /**
   * Constructeur par défaut, crée un objet aux valeurs null
   */
  public ParentEtudiant() {
    super();
    reservations = new ArrayList<>();
  }

  /**
   * Accède à la liste de toutes les réservations du membre
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
  public void ajoutReservation(Reservation reservation) {
    this.reservations.add(reservation);
  }

  /**
   * Enlève une réservation qui a été annulé
   *
   * @param index L'indice de la position de la réservation
   */
  public void supprimeReservation(int index) {
    this.reservations.remove(index);
  }
}
