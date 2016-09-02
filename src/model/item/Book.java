package model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ressources.DateParser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Book class that inherits from Item
 * Contains typical information about a book
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 * @version 1.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Book extends Item {

  private String publication;
  private ArrayList<Author> author;
  private String editor;
  private int edition;
  private Date added,
               outdated,
               removed;

  /**
   * Default constructor for Book
   */
  public Book() {
    init();
  }

  /**
   * Takes JSON data to build object
   * @param book JSON formatted book data
   */
  public Book(JSONObject book) {
    init();
    fromJSON(book);
  }

  /**
   * Initializes object with default data
   */
  public void init() {
    super.init();
    publication = "";
    author = new ArrayList<>();
    editor = "";
    edition = 0;
    added = null;
    outdated = null;
    removed = null;
  }

  /**
   * Get publication year
   *
   * @return Publication year
   */
  public String getPublication() {
    return publication;
  }

  /**
   * Set publication year
   *
   * @param publication The publication year
   */
  public void setPublication(String publication) {
    this.publication = publication;
  }

  /**
   * Ajoute un author à l'ouvrage
   *
   * @param author Un author
   */
  public void addAuthor(Author author) {
    this.author.add(author);
  }


  /**
   * Récupère un author lié à un ouvrage
   *
   * @param index L'index de l'author
   * @return author L'author situé à l'index
   */
  public Author getAuthor(int index) {
    return author.get(index);
  }

  /**
   * Récupère tous auteurs liés à l'ouvrage
   *
   * @return author Une liste des auteurs
   */
  public ArrayList<Author> getAuthors() {
    return author;
  }

  public String getAuthorString() {
    String authorString = "";

    for (Author a : author) {
      authorString += a.toString() + "; ";
    }

    return authorString;
  }

  /**
   * Supprime un author lié à un ouvrage
   *
   * @param index L'index de l'author
   */
  public void removeAuthor(int index) {
    author.remove(index);
  }

  /**
   * Supprime tous les auteurs liés à un ouvrage
   */
  public void clearAuthors() {
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

  public void setAdded(String date) {
    added = DateParser.dateFromString(date);
  }

  public Date getAdded() {
    return added;
  }

  public String getAddedString() {
    return DateParser.dateToString(added);
  }

  public void setOutdated(Date date) {
    outdated = date;
  }

  public void setOutdated(String date) {
    outdated = DateParser.dateFromString(date);
  }

  public Date getOutdated() {
    return outdated;
  }

  public String getOutdatedString() {
    return DateParser.dateToString(outdated);
  }

  public void setRemoved(Date date) {
    removed = date;
  }

  public void setRemoved(String date) {
    removed = DateParser.dateFromString(date);
  }

  public Date getRemoved() {
   return removed;
  }

  public String getRemovedString() {
    return DateParser.dateToString(removed);
  }

  public String getStatus() {
    if(removed != null) {
      return "REMOVED";
    }

    if(outdated != null) {
      return "OUTDATED";
    }

    return "VALID";
  }

  public void fromJSON(JSONObject json) {
    super.fromJSON(json);

    try {
      if (json.has("editor")) {
        editor = json.getString("editor");
      }

      if (json.has("publication")) {
        publication = json.getString("publication");
      }

      if (json.has("author")) {
        JSONArray authors = json.getJSONArray("author");

        for(int i = 0; i < authors.length(); i++) {
          this.author.add(new Author(authors.getJSONObject(i)));
        }
      }

      if (json.has("edition")) {
        edition = json.getInt("edition");
      }

      if (json.has("status")) {
        JSONObject status = json.getJSONObject("status");

        if (status.has("VALID")) {
          setAdded(status.getString("VALID"));
        }

        if (status.has("OUTDATED")) {
          setOutdated(status.getString("OUTDATED"));
        }

        if (status.has("REMOVED")) {
          setRemoved(status.getString("REMOVED"));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject book = super.toJSON();
    JSONObject status = new JSONObject();
    JSONArray authors = new JSONArray();

    try {
      for (Author author : this.author) {
        authors.put(author.toJSON());
      }

      status.put("ADDED", DateParser.dateToString(added));

      if (getStatus().equals("OUTDATED")) {
        status.put("OUTDATED", DateParser.dateToString(outdated));
      }

      if (getStatus().equals("REMOVED")) {
        status.put("OUTDATED", DateParser.dateToString(outdated));
        status.put("REMOVED", DateParser.dateToString(outdated));
      }

      book.put("editor", editor);
      book.put("edition", edition);
      book.put("publication", publication);
      book.put("author", authors);
      book.put("status", status);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return book;
  }
}
