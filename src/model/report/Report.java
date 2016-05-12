package model.report;

import java.util.Date;

/**
 * Contient les informations d'un report et les fonctions nécessaires à créer
 * des rapports.
 *
 * @author Dereck
 * @since 2015-11-21
 */
public class Report {

  private Date dateGeneree;
  private Date dateDebut;
  private Date dateFin;

  /**
   * Constructeur de report avec la date générée à d'aujourd'hui
   *
   * @param dateDebut La date de debut des statistiques du report
   * @param dateFin La date de fin des statistiques du report
   */
  public Report(Date dateDebut, Date dateFin) {
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    dateGeneree = new Date();
  }

  /**
   * Modifie la valeur de la date de debut
   *
   * @param dateDebut Date de debut du report
   */
  public void setDateDebut(Date dateDebut) {
    this.dateDebut = dateDebut;
  }

  /**
   * Récupère la valeur de la date de debut
   *
   * @return Date de debut du report
   */
  public Date getDateDebut() {
    return dateDebut;
  }

  /**
   * Modifie la valeur de la date de fin
   *
   * @param dateFin Date de fin du report
   */
  public void setDateFin(Date dateFin) {
    this.dateFin = dateFin;
  }

  /**
   * Récupère la valeur de la date de fin
   *
   * @return Date de fin du report
   */
  public Date getDateFin() {
    return dateFin;
  }

  /**
   * Récupère la date à laquelle la report a été généré
   *
   * @return Date de génération
   */
  public Date getDateGeneree() {
    return dateGeneree;
  }

  /**
   * Modifie la date à laquelle le report a été généré
   *
   * @param dateGeneree Nouvelle date
   */
  public void setDateGeneree(Date dateGeneree) {
    this.dateGeneree = dateGeneree;
  }

//   - Liste d'inventaire (nom + nombre + valeur total en stock)
//   - comptes désactivés (nb et montant) par dates
//   - livre désuet et/ou (nb et montant) par dates
}
