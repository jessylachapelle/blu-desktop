package model.item;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.DateParser;

/**
 * Book class that inherits from Item
 * Contains typical information about a book
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 * @version 1.1
 */
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
   * Ajoute un author à l'ouvrage
   *
   * @param author Un author
   */
  public void addAuthor(Author author) {
    this.author.add(author);
  }

  public void deleteAuthor(int id) {
    for (Author a : author) {
      if (a.getId() == id) {
        author.remove(a);
        return;
      }
    }
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
      _setAdded(status.optString("VALID", ""));
      _setOutdated(status.optString("OUTDATED", ""));
      _setRemoved(status.optString("REMOVED", ""));
    }
  }

  @SuppressWarnings("unused")
  public String getAddedString() {
    return DateParser.dateToString(added);
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
   * Récupère le numéro d'édition de l'ouvrage
   *
   * @return edition Numéro d'édition de l'ouvrage
   */
  public int getEdition() {
    return edition;
  }

  /**
   * Récupère l'éditeur de l'ouvrage
   *
   * @return editor Éditeur de l'ouvrage
   */
  public String getEditor() {
    return editor;
  }

  @SuppressWarnings("unused")
  public String getOutdatedString() {
    return DateParser.dateToString(outdated);
  }

  /**
   * Get publication year
   *
   * @return Publication year
   */
  public String getPublication() {
    return publication;
  }

  @SuppressWarnings("unused")
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

  public boolean isOutdated() {
    return getStatus().equals(STATUS_OUTDATED);
  }

  public boolean isRemoved() {
    return getStatus().equals(STATUS_REMOVED);
  }

  public void setAuthorId(Author author) {
    for (Author a : this.author) {
      if (a.getFirstName().equals(author.getFirstName()) && a.getLastName().equals(author.getLastName())) {
        a.setId(author.getId());
        return;
      }
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

  public void updateAuthor(Author author) {
    // TODO: What is this code?
    for (Author a : this.author) {
      if (a.getId() == author.getId()) {
        //noinspection UnusedAssignment
        a = author;
        return;
      }
    }
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

  private void _setAdded(String date) {
    added = DateParser.dateFromString(date);
  }

  private void _setOutdated(String date) {
    outdated = DateParser.dateFromString(date);
  }

  private void _setRemoved(String date) {
    removed = DateParser.dateFromString(date);
  }
}
