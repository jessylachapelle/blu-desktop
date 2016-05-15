package hanlder;

import java.util.ArrayList;
import model.article.Article;
import model.membre.Membre;

/**
 * Permet de faire une recherche dans la base de données pour y trouver des
 * articles et/ou des membres
 *
 * @author Jessy Lachapelle
 * @since 05/11/2015
 * @version 0.1
 */
public class SearchHandler {
  private String recherche;
  private boolean article,
          archive;

  /**
   * Constructeur par défaut
   */
  public void GestionnaireRecherche() {
    recherche = "";
    article = false;
    archive = false;
  }

  /**
   * Définie la chaîne de charactère de la recherche
   *
   * @param recherche La recherche à faire
   */
  public void setCritereRecherche(String recherche) {
    this.recherche = recherche;
  }

  /**
   * Défini une recherche par article
   */
  public void setRechercheArticle() {
    this.article = true;
  }

  /**
   * Défini une recherche par membre
   */
  public void setRechercheMembre() {
    this.article = false;
  }

  /**
   * Défini de chercher dans les comptes désactivés
   */
  public void rechercheDesactive() {
    this.archive = true;
  }

  /**
   * Défini de chercher dans les articles retirés
   */
  public void rechercheRetire() {
    rechercheDesactive();
  }

  /**
   * Défini de chercher seulement les comptes actifs
   */
  public void rechercheActif() {
    this.archive = false;
  }

  /**
   * Défini de chercher seulement les articles valides
   */
  public void rechercheValide() {
    rechercheActif();
  }

  /**
   * Demande aux gestionnaire de membre ou d'article de faire une liste selon
   * les propriétés définies
   *
   * @return Liste des Articles ou des Membres résultant de la recherche
   */
  public Object recherche() {
    if (article) {
      return rechercheArticle();
    }
    return rechercheMembre();
  }

  /**
   * Demande aux gestionnaire de membre de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Membres résultant de la recherche
   */
  public ArrayList<Membre> rechercheMembre() {
    MemberHandler mh = new MemberHandler();
    return mh.searchMembers(recherche, archive);
  }

  /**
   * Demande aux gestionnaire d'article de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Articles résultant de la recherche
   */
  public ArrayList<Article> rechercheArticle() {
    ItemHandler itemHandler = new ItemHandler();
    return itemHandler.searchItem(recherche, archive);
  }
}
