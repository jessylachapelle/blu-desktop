package model.member;

import java.util.Date;
import org.json.JSONObject;
import utility.DateParser;

/**
 *
 * @author Jessy
 */
public class Comment {

  private int id;
  private String comment;
  private Date date;
  private String updatedBy;

  public Comment(JSONObject comment) {
    _init();
    fromJSON(comment);
  }

  public Comment(int id, String comment) {
    _init();
    this.id = id;
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public Date getDate() {
    return date;
  }

  public int getId() {
    return id;
  }

  public void fromJSON(JSONObject comment) {
    id = comment.optInt("id", id);
    this.comment = comment.optString("comment", this.comment);
    updatedBy = comment.optString("updated_by", updatedBy);

    String date = comment.optString("updated_at", "");
    if (!date.isEmpty()) {
      setDate(date);
    }
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDate(String date) {
    this.date = DateParser.dateFromString(date);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public JSONObject toJSON() {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("comment", comment);
    json.put("updated_at", _getDateString());
    json.put("updated_by", updatedBy);

    return json;
  }

  private String _getDateString() {
    return DateParser.dateToString(date);
  }

  private void _init() {
    id = 0;
    comment = "";
    date = new Date();
    updatedBy = "";
  }
}
