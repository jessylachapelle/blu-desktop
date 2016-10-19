package model.item;

import org.json.JSONObject;

/**
 * Subject for categorizing items
 * @author Jessy Lachapele
 * @since 13/07/2016
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Subject {
  private int id;
  private String name;
  private Category category;

  /**
   * Default constructor
   */
  public Subject() {
    _init();
  }

  public Subject(JSONObject json) {
    _init();
    fromJSON(json);
  }

  private void _init() {
    id = 0;
    name = "";
    category = new Category();
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
  public Category getCategory() {
    return category;
  }

  /**
   * Set subject's category
   * @param category Subject's category
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Initialize Subject from JSON formatted data
   * @param json JSON formatted data
   */
  public void fromJSON(JSONObject json) {
    id = json.optInt("id", id);
    name = json.optString("name", name);

    JSONObject category = json.optJSONObject("category");
    if (category != null) {
      this.category.fromJSON(category);
    }
  }

  public JSONObject toJSON() {
    JSONObject subject = new JSONObject();

    subject.put("id", id);
    subject.put("name", name);
    subject.put("category", category);

    return subject;
  }

  @Override
  public String toString() {
    return name;
  }
}
