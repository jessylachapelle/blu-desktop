package model.article;

/**
 * Classe Matiere qui contient les informations concernant une matière
 *
 * @author Dereck
 * @date 26/10/2015
 */
public class Matiere {

  private String nom;
  private String categorie;

  /**
   * Constructeur par défaut
   */
  public Matiere() {
    nom = "";
    categorie = "";
  }

  /**
   * Constructeur avec paramètre de Matiere
   *
   * @param nom Nom de la matiere
   * @param categorie Categorie à laquelle appartient la matière
   */
  public Matiere(String nom, String categorie) {
    this.nom = nom;
    this.categorie = categorie;
  }

  /**
   * Récupère le nom de la matière
   *
   * @return nom
   */
  public String getNom() {
    return nom;
  }

  /**
   * Attribue une valeur au nom
   *
   * @param nom Nom de la matière
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère la catégorie
   *
   * @return categorie
   */
  public String getCategorie() {
    return categorie;
  }

  /**
   * Attribue une valeur à la catégorie
   *
   * @param categorie Catégorie de la matière
   */
  public void setCategorie(String categorie) {
    this.categorie = categorie;
  }
}
