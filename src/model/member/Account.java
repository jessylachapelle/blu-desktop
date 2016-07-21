package model.member;

import java.util.ArrayList;
import java.util.Date;

import model.item.Copy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ressources.DateParser;

/**
 * Le compte d'un member, contient tous les exemplaires mis en vente par le
 * member ainsi que les informations d'acitivité du compte
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 1.1
 */
@SuppressWarnings("unused")
public class Account {

  private Date registration,
               lastActivity;

  private ArrayList<Comment> comments;

  private ArrayList<Copy> available,
                          sold,
                          paid,
                          reserved;

  /**
   * Constructeur par défaut, crée compte à valeur null
   */
  public Account() {
    init();
  }

  public Account(JSONObject json) {
    init();
    fromJSON(json);
  }

  private void init() {
    registration = new Date();
    lastActivity = new Date();
    comments = new ArrayList<>();
    available = new ArrayList<>();
    sold = new ArrayList<>();
    paid = new ArrayList<>();
    reserved = new ArrayList<>();
  }

  /**
   * Constructeur permettant de spécifier les informations de bases du compte
   *
   * @param registration La date d'ouverture du compte
   * @param lastActivity La dernière activité enregistré au compte
   */
  public Account(Date registration, Date lastActivity) {
    setRegistration(registration);
    setLastActivity(lastActivity);

    comments = new ArrayList<>();
    available = new ArrayList<>();
    sold = new ArrayList<>();
    reserved = new ArrayList<>();
  }

  /**
   * Accède à la date d'ouverture du compte
   *
   * @return registration La date d'ouverture du compte
   */
  public Date getRegistration() {
    return registration;
  }

  /**
   * Modifie la date d'ouverture du compte
   *
   * @param registration La date d'ouverture de compte
   */
  public void setRegistration(Date registration) {
    this.registration = registration;
  }

  /**
   *
   * @param date Date au format aaaa-mm-jj
   */
  public void setRegistration(String date) {
    setRegistration(DateParser.dateFromString(date));
  }

  /**
   * Accède à la date de la dernière activité au compte
   *
   * @return dateDernièreActivite La date de la dernière activité
   */
  public Date getLastActivity() {
    return lastActivity;
  }

  /**
   * Modifie la date de la dernière activité au compte
   *
   * @param lastActivity La date de la dernière activité
   */
  public void setLastActivity(Date lastActivity) {
    this.lastActivity = lastActivity;
  }

  /**
   *
   * @param date Date au format aaaa-mm-jj
   */
  public void setLastActivity(String date) {
    setLastActivity(DateParser.dateFromString(date));
  }

  /**
   * Accède à la liste des commentaires associée au compte
   *
   * @return comments La liste des commentaires
   */
  public ArrayList<Comment> getComments() {
    return comments;
  }

  /**
   * Défini la liste des commentaires
   *
   * @param comments La liste des commentaires
   */
  public void setComments(ArrayList<Comment> comments) {
    this.comments = comments;
  }

  /**
   * Ajoute un comments à la liste
   *
   * @param comment Comment à ajouter
   */
  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  /**
   * Supprime un comments de la liste
   *
   * @param index Indice de la position du comments à supprimer
   */
  public void removeComment(int index) {
    this.comments.remove(index);
  }

  public void setCopies(ArrayList<Copy> copies) {
    for (Copy copy : copies) {
      if (copy.isReserved()) {
        reserved.add(copy);
      } else if (copy.isSold()) {
        sold.add(copy);
      } else if (copy.isPayed()) {
        paid.add(copy);
      } else {
        available.add(copy);
      }
    }
  }

  /**
   * Accède à la liste des exemplaires en vente
   *
   * @return available La liste des exemplaires en vente
   */
  public ArrayList<Copy> getAvailable() {
    return available;
  }

  /**
   * Défini la liste des exemplaires en vente
   *
   * @param available Liste des exemplaires en vente
   */
  public void setAvailable(ArrayList<Copy> available) {
    this.available = available;
  }

  /**
   * Ajout d'un copy à la liste en vente
   *
   * @param copy Copy à ajouter
   */
  public void addAvailable(Copy copy) {
    available.add(copy);
  }

  /**
   * Ajout de plusieurs copies à la liste en vente
   *
   * @param copies Liste des copies à ajouter
   */
  public void addAvailable(ArrayList<Copy> copies) {
    available.addAll(copies);
  }

  /**
   * Supprime en exemplaire de la liste available
   *
   * @param index Indice de l'exemplaire à supprimer
   */
  public void removeCopyAvailable(int index) {
    available.remove(index);
  }

  /**
   * Accède à la liste des exemplaires vendus
   *
   * @return sold La liste des exemplaires sold
   */
  public ArrayList<Copy> getSold() {
    return sold;
  }

  /**
   * Défini la liste des exemplaires vendus
   *
   * @param sold Les des exemplaires vendus
   */
  public void setSold(ArrayList<Copy> sold) {
    this.sold = sold;
  }

  /**
   * Ajout d'un copy à la liste des exemplaires vendus
   *
   * @param copy L'copy à ajouter
   */
  public void addSold(Copy copy) {
    this.sold.add(copy);
  }

  /**
   * Accède à une liste des exemplaires
   *
   * @return paid La liste des exemplaires dont l'argent a été remis au
   * member
   */
  public ArrayList<Copy> getPaid() {
    return paid;
  }

  /**
   * Défini la liste des exemplaires dont l'argent est remis
   *
   * @param paid Liste des exemplaires que l'argent a été remis au member
   */
  public void setPaid(ArrayList<Copy> paid) {
    this.paid = paid;
  }

  public void addPayed(ArrayList<Copy> copies) {
    this.paid.addAll(copies);
  }

  public void addPayed(Copy copy) {
    this.paid.add(copy);
  }

  /**
   * Accède à la liste des exemplaires mis en réservation
   *
   * @return reserved Liste des exemplaires en réservation
   */
  public ArrayList<Copy> getReserved() {
    return reserved;
  }

  /**
   * Défini la liste des exemplaires mis en réservation
   *
   * @param reserved Liste des exemplaires en réservation
   */
  public void setReserved(ArrayList<Copy> reserved) {
    this.reserved = reserved;
  }

  /**
   * Ajout d'un copy à la liste mis en réservation
   *
   * @param copy Copy à ajouter
   */
  public void addReserved(Copy copy) {
    reserved.add(copy);
  }

  /**
   * Annulation d'une réservation et remise en vente d'un exemplaire
   *
   * @param availableCopyIndex L'exemplaire à remettre en vente
   */
  public void cancelReservation(int availableCopyIndex) {
    available.add(reserved.remove(availableCopyIndex));
  }

  /**
   * Prend un exeplaire en réservation et le met dans la liste sold
   *
   * @param reservedCopyIndex L'exemplaire sold
   */
  public void sellReservedCopy(int reservedCopyIndex) {
    sold.add(reserved.remove(reservedCopyIndex));
  }

  /**
   * Prend un exemplaire de available et le met dans sold
   *
   * @param availableCopyIndex Copy sold
   */
  public void sell(int availableCopyIndex) {
    sold.add(available.remove(availableCopyIndex));
  }

  @SuppressWarnings("deprecation")
  public boolean isActive() {
    Date today = new Date();
    Date limit = new Date(today.getYear() - 1, today.getMonth(), today.getDay());
    return lastActivity.after(limit);
  }

  public boolean isInactive() {
    return !isActive();
  }

  @SuppressWarnings("deprecation")
  public boolean isDeactivated() {
    Date today = new Date();
    Date limit = new Date(today.getYear() - 1, today.getMonth() - 6, today.getDay());
    return lastActivity.before(limit);
  }

  public double amountAvailable() {
    double amount = 0;

    for (Copy copy : available) {
      amount += copy.getPrice();
    }
    return amount;
  }

  public int quantityAvailable() {
    return available.size();
  }

  public double amountSold() {
    double amount = 0;

    for (Copy copy : sold) {
      amount += copy.getPrice();
    }
    return amount;
  }

  public int quantitySold() {
    return sold.size();
  }

  public double amountPaid() {
    double amount = 0;

    for (Copy copy : paid) {
      amount += copy.getPrice();
    }
    return amount;
  }

  public int quantityPaid() {
    return paid.size();
  }

  public Comment getComment(int id) {
    for (Comment comment : comments) {
      if (comment.getId() == id) {
        return comment;
      }
    }

    return null;
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("registration")) {
        setRegistration(json.getString("registration"));
      }

      if(json.has("last_activity")) {
        setLastActivity(json.getString("last_activity"));
      }

      if (json.has("comment")) {
        JSONArray comments = json.getJSONArray("comment");

        for (int i = 0; i < comments.length(); i++) {
          this.comments.add(new Comment(comments.getJSONObject(i)));
        }
      }

      if (json.has("copies")) {
        JSONArray copies = json.getJSONArray("copies");

        for (int i = 0; i < copies.length(); i++) {
          Copy copy = new Copy(copies.getJSONObject(i));

          if (copy.isReserved()) {
            addReserved(copy);
          } else if (copy.isPayed()) {
            addPayed(copy);
          } else if (copy.isSold()) {
            addSold(copy);
          } else {
            addAvailable(copy);
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject account = new JSONObject();
    JSONArray comments = new JSONArray();
    JSONArray copies = new JSONArray();

    try {
      account.put("registration", registration);
      account.put("last_activity", lastActivity);

      for (Comment comment : this.comments) {
        comments.put(comment.toJSON());
      }

      for (Copy copy : available) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : sold) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : paid) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : reserved) {
        copies.put(copy.toJSON());
      }

      account.put("comment", comments);
      account.put("copies", copies);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return account;
  }
}
