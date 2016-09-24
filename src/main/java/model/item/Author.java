package model.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The author of a book
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 */
@SuppressWarnings("unused")
public class Author {
  String firstName;
  String lastName;

  public Author() {
    firstName = "";
    lastName = "";
  }

  public Author(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Author(JSONObject author) {
    fromJSON(author);
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

    try {
      author.put("first_name", firstName);
      author.put("last_name", lastName);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return author;
  }

  public void fromJSON(JSONObject author) {
    try {
      if (author.has("first_name")) {
        setFirstName(author.getString("first_name"));
      }

      if (author.has("last_name")) {
        setLastName(author.getString("last_name"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }
}
