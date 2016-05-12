/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.membre;

import java.util.Date;

/**
 *
 * @author Jessy
 */
public class Commentaire {

  private int id;
  private String commentaire;
  private String date;

  public Commentaire() {
    this.id = 0;
    this.commentaire = "";
    this.date = "";
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCommentaire() {
    return commentaire;
  }

  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
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
}
