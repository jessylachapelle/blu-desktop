package model.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Subject for categorizing items
 * @author Jessy Lachapele
 * @since 13/07/2016
 */
@SuppressWarnings("unused")
public class Subject {
  private int id;
  private String name;
  private String category;

  /**
   * Default constructor
   */
  public Subject() {
    id = 0;
    name = "";
    category = "";
  }

  /**
   * Constructor with subject parameters
   * @param name Subject's name
   * @param category Subject's category
   */
  public Subject(String name, String category) {
    setName(name);
    setCategory(category);
  }

  public Subject(JSONObject json) {
    fromJSON(json);
  }

  public int getId() {
    return id;
  }
  /**
   * Get subject's name
   * @return name Subject's name
   */
  public String getName() {
    return name;
  }

  /**
   * Set subject's name
   * @param name Subject's name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get subject's category
   * @return category Subject's category
   */
  public String getCategory() {
    return category;
  }

  /**
   * Set subject's category
   * @param category Subject's category
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Initialize Subject from JSON formatted data
   * @param json JSON formatted data
   */
  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id") && json.get("id") instanceof Integer) {
        id = json.getInt("id");
      }

      if (json.has("name")) {
        name = json.getString("name");
      }

      if (json.has("category") && json.get("category") instanceof String) {
        category = json.getString("category");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject subject = new JSONObject();
    try {
      subject.put("id", id);
      subject.put("name", name);
      subject.put("category", category);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return subject;
  }

  @Override
  public String toString() {
    return name;
  }
}
