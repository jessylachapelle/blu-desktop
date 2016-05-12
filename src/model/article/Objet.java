package model.article;

/**
 * Classe objet qui hérite de la classe Article et qui ajoute une description.
 *
 * @author Dereck Pouliot
 * @date 26/10/2015
 */
public class Objet extends Article {

  private String description;

  /**
   * Constructeur par défaut d'un Objet
   */
  public Objet() {
    description = "";
  }

  /**
   * Constructeur avec paramètre d'un Objet
   *
   * @param description Description de l'objet
   */
  public Objet(String description) {
    this.description = description;
  }

  /**
   * Récupère la description de l'objet
   *
   * @return description String contenant la description de l'objet
   */
  public String getDescription() {
    return description;
  }

  /**
   * Attribue une valeur à la description d'un objet
   *
   * @param description Description de l'objet
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
