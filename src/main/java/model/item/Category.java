package model.item;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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

  @Override
  public String toString() {
    return name;
  }

  public void fromJSON(JSONObject category) {
    id = category.optInt("id", id);
    name = category.optString("name", name);

    JSONArray subjects = category.optJSONArray("subject");
    if (subjects != null) {
      this.subjects.clear();

      for (int i = 0; i < subjects.length(); i++) {
        JSONObject subject = subjects.optJSONObject(i);

        if (subject != null) {
          this.subjects.add(new Subject(subject));
        }
      }
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Subject> getSubjects() {
    return subjects;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JSONObject toJSON() {
    JSONObject category = new JSONObject();

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

    return category;
  }

  private void _init() {
    id = 0;
    name = "";
    subjects = new ArrayList<>();
  }
}
