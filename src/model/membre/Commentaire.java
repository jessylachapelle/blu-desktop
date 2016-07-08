/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.membre;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 *
 * @author Jessy
 */
public class Commentaire {

  private int id;
  private String comment;
  private String date;

  public Commentaire() {
    init();
  }

  public Commentaire(JSONObject json) {
    fromJson(json, true);
  }

  private void init() {
    this.id = 0;
    this.comment = "";
    this.date = "";
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

  public String getDate() {
    return date;
  }

  public void setDate(int annee, int mois, int jour) {
    if (2000 <= annee && 1 <= mois && mois <= 12 && 1 <= jour && jour <= 31) {
      this.date = annee + "-" + mois + "-" + jour;
    }
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setDate(Date date) {
    this.date = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
  }

  public void fromJson(JSONObject json) {
    fromJson(json, false);
  }

  public void fromJson(JSONObject json, boolean reinitialize) {
    if (reinitialize) {
      init();
    }

    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("comment")) {
        comment = json.getString("comment");
      }

      if (json.has("updated_at")) {
        setDate(json.getString("updated_at"));
      }

      if (json.has("updated_by")) {
        // TODO: add employee to object
        // employee = json.getString("updated_by");
      }

      // TODO: DELETE
      if (json.has("comment")) {
        comment = json.getString("comment");
      }

      if (json.has("date")) {
        setDate(json.getString("date"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
