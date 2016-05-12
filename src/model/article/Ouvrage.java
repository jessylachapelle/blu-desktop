package model.article;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe Ouvrage qui hérite de la classe Article et qui ajoute des propriétés
 * typiques d'un ouvrage
 *
 * @author Dereck Pouliot
 * @date 26/10/2015
 */
public class Ouvrage extends Article {

  private int annee;
  private ArrayList<String> auteur;
  private String editeur;
  private int noEdition;
  private Date dateAjout,
               dateDesuet,
               dateRetire;

  /**
   * Constructeur par défaut de Ouvrage
   */
  public Ouvrage() {
    super();
    annee = 0;
    auteur = new ArrayList<>();
    editeur = "";
    noEdition = 0;
    dateAjout = null;
    dateDesuet = null;
    dateRetire = null;
  }

  /**
   * Récupère l'année de parution de l'ouvrage
   *
   * @return annee Année de parution
   */
  public int getAnnee() {
    return annee;
  }

  /**
   * Attribue une valeur à l'année de parution
   *
   * @param annee Année de parution de l'ouvrage
   */
  public void setAnnee(int annee) {
    this.annee = annee;
  }

  /**
   * Ajoute un auteur à l'ouvrage
   *
   * @param auteur Un auteur
   */
  public void ajouterAuteur(String auteur) {
    this.auteur.add(auteur);
  }

  /**
   * Ajoute une liste d'auteurs à l'ouvrage
   *
   * @param auteurs Les auteurs à ajouter
   */
  public void ajouterAuteurs(ArrayList<String> auteurs) {
    for (int nbAuteur = 0; nbAuteur < auteurs.size(); nbAuteur++) {
      auteur.add(auteur.get(nbAuteur));
    }
  }

  /**
   * Récupère un auteur lié à un ouvrage
   *
   * @param index L'index de l'auteur
   * @return auteur L'auteur situé à l'index
   */
  public String getAuteur(int index) {
    return auteur.get(index);
  }

  /**
   * Récupère tous auteurs liés à l'ouvrage
   *
   * @return auteur Une liste des auteurs
   */
  public ArrayList<String> getTousAuteurs() {
    return auteur;
  }

  public String getAuteurToString() {
    String strAuteurs = "";

    for (int noAuteur = 0; noAuteur < auteur.size(); noAuteur++) {
      strAuteurs += auteur.get(noAuteur);

      if (noAuteur != (auteur.size() - 1))
        strAuteurs += ", ";
    }
    return strAuteurs;
  }

  /**
   * Supprime un auteur lié à un ouvrage
   *
   * @param index L'index de l'auteur
   */
  public void supprimerAuteur(int index) {
    auteur.remove(index);
  }

  /**
   * Supprime tous les auteurs liés à un ouvrage
   */
  public void supprimerTousAuteur() {
    auteur.clear();
  }

  /**
   * Récupère l'éditeur de l'ouvrage
   *
   * @return editeur Éditeur de l'ouvrage
   */
  public String getEditeur() {
    return editeur;
  }

  /**
   * Attribue une valeur à l'éditeur de l'ouvrage
   *
   * @param editeur Éditeur de l'ouvrage
   */
  public void setEditeur(String editeur) {
    this.editeur = editeur;
  }

  /**
   * Récupère le numéro d'édition de l'ouvrage
   *
   * @return noEdition Numéro d'édition de l'ouvrage
   */
  public int getNoEdition() {
    return noEdition;
  }

  /**
   * Attribue une valeur au numéro d'édition de l'ouvrage
   *
   * @param noEdition Numéro d'édition de l'ouvrage
   */
  public void setNoEdition(int noEdition) {
    this.noEdition = noEdition;
  }

  public void setDateAjout(Date date) {
    dateAjout = date;
  }

  public void setDateAjout(String date) {
    dateAjout = stringToDate(date);
  }

  public void setDateDesuet(Date date) {
    dateDesuet = date;
  }

  public void setDateDesuet(String date) {
    dateDesuet = stringToDate(date);
  }

  public void setDateRetire(Date date) {
    dateRetire = date;
  }

  public void setDateRetire(String date) {
    dateRetire = stringToDate(date);
  }

  public String getStatut() {
    if(dateRetire != null)
      return "Retiré";
    else if(dateDesuet != null)
      return "Désuet";
    return "Valide";
  }

  private Date stringToDate(String strDate) {
    if(strDate.isEmpty())
      return null;

    Date date = new Date();

    String strAnnee = strDate.substring(0, 4);
    String strMois = strDate.substring(5, 7);
    String strJour = strDate.substring(8, 10);

    date.setYear(Integer.parseInt(strAnnee) - 1900);
    date.setMonth(Integer.parseInt(strMois) - 1);
    date.setDate(Integer.parseInt(strJour));

    return date;
  }
}
