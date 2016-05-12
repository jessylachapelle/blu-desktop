package model.membre;

import java.util.ArrayList;
import java.util.Date;
import model.article.Exemplaire;
import model.transaction.Transaction;

/**
 * Le compte d'un membre, contient tous les exemplaires mis en vente par le
 * membre ainsi que les informations d'acitivité du compte
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.2
 */
public class Compte {

  private Date dateCreation,
               dateDerniereActivite;
  private double solde;

  private ArrayList<Transaction> transactions;
  private ArrayList<Commentaire> commentaire;

  private ArrayList<Exemplaire> enVente,
                                vendu,
                                argentRemis,
                                enReservation;

  /**
   * Constructeur par défaut, crée compte à valeur null
   */
  public Compte() {
    dateCreation = new Date();
    dateDerniereActivite = new Date();
    solde = 0;
    transactions = new ArrayList<>();
    commentaire = new ArrayList<>();
    enVente = new ArrayList<>();
    vendu = new ArrayList<>();
    argentRemis = new ArrayList<>();
    enReservation = new ArrayList<>();
  }

  /**
   * Constructeur permettant de spécifier les informations de bases du compte
   *
   * @param dateCreation La date d'ouverture du compte
   * @param dateDerniereActivite La dernière activité enregistré au compte
   */
  public Compte(Date dateCreation, Date dateDerniereActivite) {
    setDateCreation(dateCreation);
    setDateDerniereActivite(dateDerniereActivite);

    transactions = new ArrayList<>();
    commentaire = new ArrayList<>();
    enVente = new ArrayList<>();
    vendu = new ArrayList<>();
    enReservation = new ArrayList<>();
  }

  /**
   * Accède à la date d'ouverture du compte
   *
   * @return dateCreation La date d'ouverture du compte
   */
  public Date getDateCreation() {
    return dateCreation;
  }

  /**
   * Modifie la date d'ouverture du compte
   *
   * @param dateCreation La date d'ouverture de compte
   */
  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  /**
   * Accède à la date de la dernière activité au compte
   *
   * @return dateDernièreActivite La date de la dernière activité
   */
  public Date getDateDerniereActivite() {
    return dateDerniereActivite;
  }

  /**
   * Modifie la date de la dernière activité au compte
   *
   * @param dateDerniereActivite La date de la dernière activité
   */
  public void setDateDerniereActivite(Date dateDerniereActivite) {
    this.dateDerniereActivite = dateDerniereActivite;
  }

  /**
   * Accède au solde du compte
   *
   * @return solde Le solde du compte
   */
  public double getSolde() {
    return solde;
  }

  /**
   * Modifie le solde du compte
   *
   * @param solde Le solde du compte
   */
  public void setSolde(double solde) {
    this.solde = solde;
  }

  /**
   * Accède à la liste des transactions associées au compte
   *
   * @return transactions La liste des transactions
   */
  public ArrayList<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Défini la liste des transactions associées au compte
   *
   * @param transactions La liste des transactions
   */
  public void setTransactions(ArrayList<Transaction> transactions) {
    this.transactions = transactions;
  }

  /**
   * ajoute une transaction à la liste
   *
   * @param transaction Nouvelle transaction
   */
  public void ajoutTransaction(Transaction transaction) {
    this.transactions.add(transaction);
  }

  /**
   * Supprime une transaction spécifié de la liste
   *
   * @param index Indice de la transaction à supprimer
   */
  public void supprimeTransaction(int index) {
    this.transactions.remove(index);
  }

  /**
   * Accède à la liste des commentaires associée au compte
   *
   * @return commentaire La liste des commentaires
   */
  public ArrayList<Commentaire> getCommentaire() {
    return commentaire;
  }

  /**
   * Défini la liste des commentaires
   *
   * @param commentaire La liste des commentaires
   */
  public void setCommentaire(ArrayList<Commentaire> commentaire) {
    this.commentaire = commentaire;
  }

  /**
   * Ajoute un commentaire à la liste
   *
   * @param commentaire Commentaire à ajouter
   */
  public void ajoutCommentaire(Commentaire commentaire) {
    this.commentaire.add(commentaire);
  }

  /**
   * Supprime un commentaire de la liste
   *
   * @param index Indice de la position du commentaire à supprimer
   */
  public void supprimeCommentaire(int index) {
    this.commentaire.remove(index);
  }

  public void setExemplaires(ArrayList<Exemplaire> exemplaires) {
    for(int noExemplaire = 0; noExemplaire < exemplaires.size(); noExemplaire++) {
      if(exemplaires.get(noExemplaire).estReserve())
        enReservation.add(exemplaires.get(noExemplaire));
      else if(exemplaires.get(noExemplaire).estVendu())
        vendu.add(exemplaires.get(noExemplaire));
      else if(exemplaires.get(noExemplaire).estRemis())
        argentRemis.add(exemplaires.get(noExemplaire));
      else
        enVente.add(exemplaires.get(noExemplaire));
    }
  }

  /**
   * Accède à la liste des exemplaires en vente
   *
   * @return enVente La liste des exemplaires en vente
   */
  public ArrayList<Exemplaire> getEnVente() {
    return enVente;
  }

  /**
   * Défini la liste des exemplaires en vente
   *
   * @param enVente Liste des exemplaires en vente
   */
  public void setEnVente(ArrayList<Exemplaire> enVente) {
    this.enVente = enVente;
  }

  /**
   * Ajout d'un exemplaire à la liste en vente
   *
   * @param exemplaire Exemplaire à ajouter
   */
  public void ajoutEnVente(Exemplaire exemplaire) {
    enVente.add(exemplaire);
  }

  /**
   * Ajout de plusieurs exemplaires à la liste en vente
   *
   * @param exemplaires Liste des exemplaires à ajouter
   */
  public void ajoutEnVente(ArrayList<Exemplaire> exemplaires) {
    for (int noExemplaire = 0; noExemplaire < exemplaires.size(); noExemplaire++) {
      enVente.add(exemplaires.get(noExemplaire));
    }
  }

  /**
   * Supprime en exemplaire de la liste enVente
   *
   * @param index Indice de l'exemplaire à supprimer
   */
  public void supprimeExemplaire(int index) {
    enVente.remove(index);
  }

  /**
   * Accède à la liste des exemplaires vendus
   *
   * @return vendu La liste des exemplaires vendu
   */
  public ArrayList<Exemplaire> getVendu() {
    return vendu;
  }

  /**
   * Défini la liste des exemplaires vendus
   *
   * @param vendu Les des exemplaires vendus
   */
  public void setVendu(ArrayList<Exemplaire> vendu) {
    this.vendu = vendu;
  }

  /**
   * Ajout d'un exemplaire à la liste des exemplaires vendus
   *
   * @param exemplaire L'exemplaire à ajouter
   */
  public void ajoutVendu(Exemplaire exemplaire) {
    this.vendu.add(exemplaire);
  }

  /**
   * Accède à une liste des exemplaires
   *
   * @return argentRemis La liste des exemplaires dont l'argent a été remis au
   * membre
   */
  public ArrayList<Exemplaire> getArgentRemis() {
    return argentRemis;
  }

  /**
   * Défini la liste des exemplaires dont l'argent est remis
   *
   * @param argentRemis Liste des exemplaires que l'argent a été remis au membre
   */
  public void setArgentRemis(ArrayList<Exemplaire> argentRemis) {
    this.argentRemis = argentRemis;
  }

  public void ajoutArgentRemis(ArrayList<Exemplaire> exemplaires) {
    for (int noExemplaire = 0; noExemplaire < exemplaires.size(); noExemplaire++) {
      this.argentRemis.add(exemplaires.get(noExemplaire));
    }
  }

  public void ajoutArgentRemis(Exemplaire exemplaire) {
    this.argentRemis.add(exemplaire);
  }

  /**
   * Accède à la liste des exemplaires mis en réservation
   *
   * @return enReservation Liste des exemplaires en réservation
   */
  public ArrayList<Exemplaire> getReserve() {
    return enReservation;
  }

  /**
   * Défini la liste des exemplaires mis en réservation
   *
   * @param enReservation Liste des exemplaires en réservation
   */
  public void setReserve(ArrayList<Exemplaire> enReservation) {
    this.enReservation = enReservation;
  }

  /**
   * Ajout d'un exemplaire à la liste mis en réservation
   *
   * @param exemplaire Exemplaire à ajouter
   */
  public void ajoutReservation(Exemplaire exemplaire) {
    enReservation.add(exemplaire);
  }

  /**
   * Annulation d'une réservation et remise en vente d'un exemplaire
   *
   * @param exemplaireEnVente L'exemplaire à remettre en vente
   */
  public void annuleReservation(int exemplaireEnVente) {
    enVente.add(enReservation.remove(exemplaireEnVente));
  }

  /**
   * Prend un exeplaire en réservation et le met dans la liste vendu
   *
   * @param exemplaireVendu L'exemplaire vendu
   */
  public void vendreReservation(int exemplaireVendu) {
    Exemplaire exemplaire = enReservation.remove(exemplaireVendu);
    vendu.add(exemplaire);
    solde += exemplaire.getPrix();
  }

  /**
   * Prend un exemplaire de enVente et le met dans vendu
   *
   * @param exemplaireVendu Exemplaire vendu
   */
  public void vendre(int exemplaireVendu) {
    Exemplaire exemplaire = enVente.remove(exemplaireVendu);
    vendu.add(exemplaire);
    solde += exemplaire.getPrix();
  }

  public boolean estActif() {
    Date aujourdhui = new Date();
    Date limite = new Date(aujourdhui.getYear() - 1,
            aujourdhui.getMonth(),
            aujourdhui.getDay());

    return dateDerniereActivite.after(limite);
  }

  public boolean estInactif() {
    return !estActif();
  }

  public boolean estDesactivé() {
    Date aujourdhui = new Date();
    Date limite = new Date(aujourdhui.getYear() - 1,
            aujourdhui.getMonth() - 6,
            aujourdhui.getDay());

    return dateDerniereActivite.before(limite);
  }

  public double montantEnVente() {
    double montant = 0;

    for (int noEnVente = 0; noEnVente < enVente.size(); noEnVente++) {
      montant += enVente.get(noEnVente).getPrix();
    }
    return montant;
  }

  public int nombreEnVente() {
    return enVente.size();
  }

  public double montantDu() {
    double montant = 0;

    for (int noVendu = 0; noVendu < vendu.size(); noVendu++) {
      montant += vendu.get(noVendu).getPrix();
    }
    return montant;
  }

  public int nombreDu() {
    return vendu.size();
  }

  public double montantRemis() {
    double montant = 0;

    for (int noRemis = 0; noRemis < argentRemis.size(); noRemis++) {
      montant += argentRemis.get(noRemis).getPrix();
    }
    return montant;
  }

  public int nombreRemis() {
    return argentRemis.size();
  }
}
