package handler;

import java.util.ArrayList;
import model.article.Article;
import model.membre.Membre;

/**
 * Permet de faire une searchQuery dans la base de données pour y trouver des
 * articles et/ou des membres
 *
 * @author Jessy Lachapelle
 * @since 05/11/2015
 * @version 0.1
 */
public class SearchHandler {
  private MemberHandler memberHandler;
  private ItemHandler itemHandler;
  private String searchQuery;
  private boolean item,
                  archive;

  /**
   * Constructeur par défaut
   */
  public SearchHandler() {
    memberHandler = new MemberHandler();
    itemHandler = new ItemHandler();
    searchQuery = "";
    item = false;
    archive = false;
  }

  /**
   * Définie la chaîne de charactère de la searchQuery
   *
   * @param searchQuery La searchQuery à faire
   */
  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  /**
   * Défini une searchQuery par item
   */
  public void setItemSearch() {
    this.item = true;
  }

  /**
   * Défini une searchQuery par membre
   */
  public void setMemberSearch() {
    this.item = false;
  }

  /**
   * Défini de chercher dans les comptes désactivés
   */
  public void setSearchArchives() {
    this.archive = true;
  }

  /**
   * Défini de chercher seulement les comptes actifs
   */
  public void setSearchActive() {
    this.archive = false;
  }

  /**
   * Demande aux gestionnaire de membre de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Membres résultant de la searchQuery
   */
  public ArrayList<Membre> searchMembers() {
    return memberHandler.searchMembers(searchQuery, archive);
  }

  /**
   * Demande aux gestionnaire d'item de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Articles résultant de la searchQuery
   */
  public ArrayList<Article> searchItems() {
    return itemHandler.searchItem(searchQuery, archive);
  }
}
