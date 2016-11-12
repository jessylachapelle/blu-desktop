package model.item;

import org.json.JSONArray;
import org.json.JSONObject;
import utility.DateParser;

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
  public static final String STATUS_VALID = "VALID";
  public static final String STATUS_OUTDATED = "OUTDATED";
  public static final String STATUS_REMOVED = "REMOVED";

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

  public void setAuthorId(Author author) {
    for (Author a : this.author) {
      if (a.getFirstName().equals(author.getFirstName()) && a.getLastName().equals(author.getLastName())) {
        a.setId(author.getId());
        return;
      }
    }
  }

  /**
   * Supprime un author lié à un ouvrage
   *
   * @param index L'index de l'author
   */
  public void removeAuthor(int index) {
    author.remove(index);
  }

  public void deleteAuthor(int id) {
    for (Author a : author) {
      if (a.getId() == id) {
        author.remove(a);
        return;
      }
    }
  }

  public void updateAuthor(Author author) {
    for (Author a : this.author) {
      if (a.getId() == author.getId()) {
        a = author;
        return;
      }
    }
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

  public void updateStatus(String status, Date date) {
    switch (status) {
      case STATUS_VALID:
        added = date;
        outdated = null;
        removed = null;
        break;
      case STATUS_OUTDATED:
        outdated = date;
        removed = null;
        break;
      case STATUS_REMOVED:
        removed = date;
        break;
    }
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

  public boolean isValid() {
    return getStatus().equals("VALID");
  }

  public boolean isOutdated() {
    return getStatus().equals("OUTDATED");
  }

  public boolean isRemoved() {
    return getStatus().equals("REMOVED");
  }

  public void fromJSON(JSONObject json) {
    super.fromJSON(json);

    editor = json.optString("editor", editor);
    publication = json.optString("publication", publication);
    edition = json.optInt("edition", edition);

    JSONArray authors = json.optJSONArray("author");
    if (authors != null) {
      this.author.clear();

      for(int i = 0; i < authors.length(); i++) {
        JSONObject author = authors.optJSONObject(i);

        if (author != null) {
          this.author.add(new Author(author));
        }
      }
    }

    JSONObject status = json.optJSONObject("status");
    if (status != null) {
      setAdded(status.optString("VALID", ""));
      setOutdated(status.optString("OUTDATED", ""));
      setRemoved(status.optString("REMOVED", ""));
    }
  }

  public JSONObject toJSON() {
    JSONObject book = super.toJSON();
    JSONObject status = new JSONObject();
    JSONArray authors = new JSONArray();

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

    return book;
  }
}
