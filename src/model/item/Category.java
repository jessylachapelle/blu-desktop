package model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author jessy on 05/08/16.
 */
public class Category {
  private int id;
  private String name;
  private ArrayList<Subject> subjects;

  public Category() {
    _init();
  }

  public Category(JSONObject category) {
    _init();
    fromJSON(category);
  }

  private void _init() {
    id = 0;
    name = "";
    subjects = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Subject> getSubjects() {
    return subjects;
  }

  public void fromJSON(JSONObject category) {
    try {
      if (category.has("id") && category.get("id") instanceof Integer) {
        id = category.getInt("id");
      }

      if (category.has("name") && category.get("name") instanceof String) {
        name = category.getString("name");
      }

      if (category.has("subject") && category.get("subject") instanceof JSONArray) {
        JSONArray subjects = category.getJSONArray("subject");

        for (int i = 0; i < subjects.length(); i++) {
          if (subjects.get(i) instanceof JSONObject) {
            this.subjects.add(new Subject(subjects.getJSONObject(i)));
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject category = new JSONObject();

    try {
      if (id > 0) {
        category.put("id", id);
      }

      if (!name.isEmpty()) {
        category.put("name", name);
      }

      if (subjects.size() > 0) {
        JSONArray subjects = new JSONArray();

        for (Subject subject : this.subjects) {
          subjects.put(subject.toJSON());
        }

        category.put("subject", subjects);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return category;
  }

  @Override
  public String toString() {
    return name;
  }
}
