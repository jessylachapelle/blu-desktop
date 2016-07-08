package model.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

  private int publication;
  private ArrayList<String> author;
  private String editor;
  private int edition;
  private Date added,
               outdated,
               removed;

  /**
   * Constructeur par défaut de Ouvrage
   */
  public Ouvrage() {
    super();
  }

  public Ouvrage(JSONObject json) {
    super();
    init();
    fromJSON(json);
  }

  private void init() {
    publication = 0;
    author = new ArrayList<>();
    editor = "";
    edition = 0;
    added = null;
    outdated = null;
    removed = null;
  }

  /**
   * Récupère l'année de parution de l'ouvrage
   *
   * @return publication Année de parution
   */
  public int getPublication() {
    return publication;
  }

  /**
   * Attribue une valeur à l'année de parution
   *
   * @param publication Année de parution de l'ouvrage
   */
  public void setPublication(int publication) {
    this.publication = publication;
  }

  /**
   * Ajoute un author à l'ouvrage
   *
   * @param author Un author
   */
  public void addAuthor(String author) {
    this.author.add(author);
  }


  /**
   * Récupère un author lié à un ouvrage
   *
   * @param index L'index de l'author
   * @return author L'author situé à l'index
   */
  public String getAuthor(int index) {
    return author.get(index);
  }

  /**
   * Récupère tous auteurs liés à l'ouvrage
   *
   * @return author Une liste des auteurs
   */
  public ArrayList<String> getAuthors() {
    return author;
  }

  public String getAuthorString() {
    String authorString = "";

    for (int i = 0; i < author.size(); i++) {
      authorString += author.get(i);

      if (i != (author.size() - 1))
        authorString += ", ";
    }
    return authorString;
  }

  /**
   * Supprime un author lié à un ouvrage
   *
   * @param index L'index de l'author
   */
  public void supprimerAuteur(int index) {
    author.remove(index);
  }

  /**
   * Supprime tous les auteurs liés à un ouvrage
   */
  public void supprimerTousAuteur() {
    author.clear();
  }

  /**
   * Récupère l'éditeur de l'ouvrage
   *
   * @return editor Éditeur de l'ouvrage
   */
  public String getEditor() {
    return editor;
  }

  /**
   * Attribue une valeur à l'éditeur de l'ouvrage
   *
   * @param editor Éditeur de l'ouvrage
   */
  public void setEditor(String editor) {
    this.editor = editor;
  }

  /**
   * Récupère le numéro d'édition de l'ouvrage
   *
   * @return edition Numéro d'édition de l'ouvrage
   */
  public int getEdition() {
    return edition;
  }

  /**
   * Attribue une valeur au numéro d'édition de l'ouvrage
   *
   * @param edition Numéro d'édition de l'ouvrage
   */
  public void setEdition(int edition) {
    this.edition = edition;
  }

  public void setAdded(Date date) {
    added = date;
  }

  public void setDateAjout(String date) {
    added = stringToDate(date);
  }

  public String getAdded() {
    return added.toString();
  }

  public void setOutdated(Date date) {
    outdated = date;
  }

  public void setDateDesuet(String date) {
    outdated = stringToDate(date);
  }

  public String getOutdated() {
    return outdated.toString();
  }

  public void setRemoved(Date date) {
    removed = date;
  }

  public void setDateRetire(String date) {
    removed = stringToDate(date);
  }

  public String getRemoved() {
    return removed.toString();
  }

  public String getStatut() {
    if(removed != null)
      return "Retiré";
    else if(outdated != null)
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

  public void fromJSON(JSONObject json) {
    super.fromJSON(json);

    try {
      if (json.has("editor")) {
        editor = json.getString("editor");
      }

      if (json.has("publication")) {
        publication = json.getInt("publication");
      }

      if (json.has("author")) {
        JSONArray authors = json.getJSONArray("author");

        for(int i = 0; i < authors.length(); i++) {
          JSONObject author = authors.getJSONObject(i);

          this.author.add(author.getString("first_name") + " " + author.getString("last_name"));
        }
      }

      if (json.has("edition")) {
        edition = json.getInt("edition");
      }

      if (json.has("status")) {
        JSONObject status = json.getJSONObject("status");

        if (status.has("VALID")) {
          setDateAjout(status.getString("VALID"));
        }

        if (status.has("OUTDATED")) {
          setDateDesuet(status.getString("OUTDATED"));
        }

        if (status.has("REMOVED")) {
          setDateRetire(status.getString("REMOVED"));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
