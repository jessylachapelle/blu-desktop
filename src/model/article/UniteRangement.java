package model.article;

import java.util.ArrayList;

/**
 * Classe uniteRangement qui contient les informations d'une unité de rangement
 *
 * @author Dereck
 * @date 26/10/2015
 */
public class UniteRangement {

  private String code;
  private ArrayList<Article> contenu;

  /**
   * Constructeur de base
   */
  public UniteRangement() {
    code = "";
    contenu = new ArrayList<Article>();
  }

  /**
   * Récupère le code
   *
   * @return code Code de l'unite de rangement
   */
  public String getCode() {
    return code;
  }

  /**
   * Attribue une valeur au code
   *
   * @param code Code de l'unite de rangement
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Ajoute un article dans l'unité de rangement
   *
   * @param article Un article
   */
  public void ajouterArticle(Article article) {
    this.contenu.add(article);
  }

  /**
   * Ajoute un array d'article dans l'unité de rangement
   *
   * @param article Un article
   */
  public void ajouterArticles(ArrayList<Article> article) {
    for (int nbArticle = 0; nbArticle < article.size(); nbArticle++) {
      contenu.add(article.get(nbArticle));
    }
  }

  /**
   * Récupère un article de l'unité de rangement
   *
   * @param index L'index de l'article
   * @return article Article à l'index
   */
  public Article getArticle(int index) {
    return contenu.get(index);
  }

  /**
   * Récupère tous les articles contenu dans l'unité de rangement
   *
   * @return contenu Une liste d'articles
   */
  public ArrayList<Article> getTousArticles() {
    return contenu;
  }

  /**
   * Supprime un article de l'unité de rangement
   *
   * @param index L'index de l'article
   */
  public void supprimerArticle(int index) {
    contenu.remove(index);
  }

  /**
   * Supprime tous les articles contenu dans l'unité de rangement
   */
  public void supprimerTousArticles() {
    contenu.clear();
  }

}
