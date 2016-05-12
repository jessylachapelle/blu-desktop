package model.transaction;

import java.util.Date;
import model.membre.ParentEtudiant;

/**
 * Une transaction de type Réservation qui ne peut être fait que par les
 * parents-étudiants
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class Reservation extends Transaction {

  ParentEtudiant titulaire;

  /**
   * Constructeur par défaut, crée une réservation aux valeurs null
   */
  public Reservation() {
    super();
    titulaire = new ParentEtudiant();
  }

  //TODO Déterminer les identifiants pour les différentes tâches
  /**
   * Constructeur qui crée une réservation avec les informations de la
   * transaction
   *
   * @param date La date de la transaction
   * @param noExemplaire L'exemplaire associé
   * @param noCompte Le membre associé
   */
  public Reservation(Date date,
          int noExemplaire, int noCompte) {
    super(0, date, noExemplaire, noCompte);
    titulaire = new ParentEtudiant();
  }

  /**
   * Accède au titulaire de la réservation
   *
   * @return titutlaire Le titulaire de la réservation
   */
  public ParentEtudiant getTitulaire() {
    return titulaire;
  }

  /**
   * Modifie le titulaire de la réservation
   *
   * @param titulaire Le titulaire de la réservation
   */
  public void setTitulaire(ParentEtudiant titulaire) {
    this.titulaire = titulaire;
  }
}
