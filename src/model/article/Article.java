package model.article;

import java.util.ArrayList;

/**
 * Classe article qui contient les informations de base d'un article
 *
 * @author Dereck
 * @date 26/10/2015
 */
public class Article {

  private int noArticle;
  private String nom;
  private int etat;
  private Matiere matiere;
  private String codeBar;
  private ArrayList<UniteRangement> uniteRangement;
  private String commentaire;

  private ArrayList<Exemplaire> enVente,
                                vendu,
                                argentRemis,
                                enReservation;

  /**
   * Constructeur sans paramètre de la classe Article
   */
  public Article() {
    noArticle = 0;
    nom = "";
    etat = 0;
    matiere = new Matiere();
    codeBar = "";
    uniteRangement = new ArrayList<>();
    commentaire = "";
    enVente = new ArrayList<>();
    vendu = new ArrayList<>();
    argentRemis = new ArrayList<>();
    enReservation = new ArrayList<>();
  }

  /**
   * Récupère le numéro de l'article
   *
   * @return noArticle
   */
  public int getNoArticle() {
    return noArticle;
  }

  /**
   * Attribue une valeur au numéro d'article
   *
   * @param noArticle
   */
  public void setNoArticle(int noArticle) {
    this.noArticle = noArticle;
  }

  /**
   * Récupère le nom
   *
   * @return nom
   */
  public String getNom() {
    return nom;
  }

  /**
   * Attribue une valeur au nom d'article
   *
   * @param nom
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère l'état
   *
   * @return etat
   */
  public int getEtat() {
    return etat;
  }

  /**
   * Attribue une valeur à l'état de l'article
   *
   * @param etat
   */
  public void setEtat(int etat) {
    this.etat = etat;
  }

  /**
   * Récupère la matiere
   *
   * @return matiere
   */
  public Matiere getMatiere() {
    return matiere;
  }

  /**
   * Attribue une valeur à la matière
   *
   * @param matiere
   */
  public void setMatiere(Matiere matiere) {
    this.matiere = matiere;
  }

  /**
   * Récupère le code barres
   *
   * @return codeBar
   */
  public String getCodeBar() {
    return codeBar;
  }

  /**
   * Attribue une valeur au code barres
   *
   * @param codeBar
   */
  public void setCodeBar(String codeBar) {
    this.codeBar = codeBar;
  }

  /**
   * Récupère l'unité de rangement
   *
   * @return uniteRangement
   */
  public ArrayList<UniteRangement> getUniteRangement() {
    return uniteRangement;
  }

  /**
   * Attribue une valeur a l'unité de rangement de l'article
   *
   * @param uniteRangement
   */
  public void setUniteRangement(ArrayList<UniteRangement> uniteRangement) {
    this.uniteRangement = uniteRangement;
  }

  public void ajouterUniteRangement(UniteRangement uniteRangement) {
    this.uniteRangement.add(uniteRangement);
  }

  public void ajouterUniteRangement(ArrayList<UniteRangement> uniteRangement) {
    for(int noUnite = 0; noUnite < uniteRangement.size(); noUnite++)
      this.uniteRangement.add(uniteRangement.get(noUnite));
  }

  /**
   * Ajoute un commentaire à l'article
   * @param commentaire Un commentaire
   */
  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
  }

  /**
   * Récupère le commentaire lié à un article
   * @return Le commentaire
   */
  public String getCommentaire() {
    return commentaire;
  }

  public String rangementStr() {
    String str = "";

    for(int noUnite = 0; noUnite < uniteRangement.size(); noUnite++)
      str += uniteRangement.get(noUnite).getCode() + ";";

    return str;
  }

  public void setRangement(String rangement) {
    uniteRangement.clear();
    String[] caisses =  rangement.split(";");

    for(int noCaisse = 0; noCaisse < caisses.length; noCaisse++) {
      UniteRangement ur = new UniteRangement();

      ur.setCode(caisses[noCaisse]);
      uniteRangement.add(ur);
    }
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
  }

  /**
   * Prend un exemplaire de enVente et le met dans vendu
   *
   * @param exemplaireVendu Exemplaire vendu
   */
  public void vendre(int exemplaireVendu) {
    Exemplaire exemplaire = enVente.remove(exemplaireVendu);
    vendu.add(exemplaire);
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
