package handler;

import java.util.ArrayList;
import model.item.Item;
import model.member.Member;

/**
 * Permet de faire une searchQuery dans la base de données pour y trouver des
 * articles et/ou des membres
 *
 * @author Jessy Lachapelle
 * @since 05/11/2015
 * @version 0.1
 */
@SuppressWarnings("unused")
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
    item = true;
  }

  /**
   * Défini une searchQuery par member
   */
  public void setMemberSearch() {
    item = false;
  }

  /**
   * Défini de chercher dans les comptes désactivés
   */
  public void setSearchArchives(boolean archive) {
    this.archive = archive;
  }

  public boolean isItemSearch() {
    return item;
  }

  public boolean isMemberSearch() {
    return !item;
  }

  /**
   * Demande aux gestionnaire de member de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Membres résultant de la searchQuery
   */
  public ArrayList<Member> searchMembers() {
    return memberHandler.searchMembers(searchQuery, archive);
  }

  /**
   * Demande aux gestionnaire d'item de faire une liste selon les propriétés
   * définies
   *
   * @return Liste des Articles résultant de la searchQuery
   */
  public ArrayList<Item> searchItems() {
    return itemHandler.searchItem(searchQuery, archive);
  }
}
