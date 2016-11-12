/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.member;

import org.json.JSONObject;
import utility.DateParser;

import java.util.Date;

/**
 *
 * @author Jessy
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Comment {

  private int id;
  private String comment;
  private Date date;
  private String updatedBy;

  public Comment() {
    init();
  }

  public Comment(JSONObject comment) {
    init();
    fromJSON(comment);
  }

  public Comment(int id, String comment) {
    init();
    this.id = id;
    this.comment = comment;
  }

  public Comment(int id, String comment, String updatedBy) {
    init();
    this.id = id;
    this.comment = comment;
    this.updatedBy = updatedBy;
  }

  public Comment(int id, String comment, Date date, String updatedBy) {
    init();
    this.id = id;
    this.comment = comment;
    this.date = date;
    this.updatedBy = updatedBy;
  }

  private void init() {
    id = 0;
    comment = "";
    date = new Date();
    updatedBy = "";
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getDate() {
    return date;
  }

  public String getDateString() {
    return DateParser.dateToString(date);
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDate(String date) {
    this.date = DateParser.dateFromString(date);
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
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

  public JSONObject toJSON() {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("comment", comment);
    json.put("updated_at", getDateString());
    json.put("updated_by", updatedBy);

    return json;
  }
}
