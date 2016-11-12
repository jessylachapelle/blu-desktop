package model.item;

import org.json.JSONObject;

/**
 * The author of a book
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Author {
  private int id;
  private String firstName;
  private String lastName;

  public Author() {
    id = 0;
    firstName = "";
    lastName = "";
  }

  public Author(int id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Author(JSONObject author) {
    fromJSON(author);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public JSONObject toJSON() {
    JSONObject author = new JSONObject();

    author.put("id", id);
    author.put("first_name", firstName);
    author.put("last_name", lastName);

    return author;
  }

  public void fromJSON(JSONObject author) {
    id = author.optInt("id", id);
    firstName = author.optString("first_name", firstName);
    lastName = author.optString("last_name", lastName);
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }
}
