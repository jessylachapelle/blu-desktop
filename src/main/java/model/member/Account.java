package model.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import model.item.Copy;
import model.item.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utility.DateParser;

/**
 * Le compte d'un member, contient tous les exemplaires mis en vente par le
 * member ainsi que les informations d'acitivité du compte
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 1.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Account {

  private Date registration,
               lastActivity;

  private ArrayList<Comment> comments;

  private ArrayList<Copy> copies;
  private ArrayList<Reservation> reservation;

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
    copies = new ArrayList<>();
    reservation = new ArrayList<>();
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
    copies = new ArrayList<>();
  }

  /**
   * Accède à la date d'ouverture du compte
   *
   * @return registration La date d'ouverture du compte
   */
  public Date getRegistration() {
    return registration;
  }

  public String getRegistrationString() {
    return DateParser.toLongDate(registration);
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

  public String getLastActivityString() {
    return DateParser.toLongDate(lastActivity);
  }

  @SuppressWarnings("deprecation")
  public Date getDeactivation() {
    Date deactivation = (Date) getLastActivity().clone();
    deactivation.setYear(deactivation.getYear() + 1);
    return deactivation;
  }

  public String getDeactivationString() {
    return DateParser.toLongDate(getDeactivation());
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
   * Removes a comment form the list
   * @param id Id of the comment to remove
   */
  public void removeComment(int id) {
    for (int i = 0; i < comments.size(); i++) {
      if (comments.get(i).getId() == id) {
        comments.remove(i);
        break;
      }
    }
  }

  public void editComment(int id, String comment, String updatedBy) {
    for (Comment c : comments) {
      if (c.getId() == id) {
        c.setComment(comment);
        c.setUpdatedBy(updatedBy);
        break;
      }
    }
  }

  public void setCopies(ArrayList<Copy> copies) {
    this.copies.addAll(copies);
  }

  public ArrayList<Copy> getCopies() {
    return copies;
  }

  public Copy getCopy(int id) {
    for (Copy copy : copies) {
      if (copy.getId() == id) {
        return copy;
      }
    }

    return null;
  }

  /**
   * Accède à la liste des exemplaires en vente
   *
   * @return available La liste des exemplaires en vente
   */
  public ArrayList<Copy> getAvailable() {
    return copies.stream().filter(Copy::isAvailable).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Accède à la liste des exemplaires vendus
   *
   * @return sold La liste des exemplaires sold
   */
  public ArrayList<Copy> getSold() {
    return copies.stream().filter(Copy::isSold).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Accède à une liste des exemplaires
   *
   * @return paid La liste des exemplaires dont l'argent a été remis au
   * member
   */
  public ArrayList<Copy> getPaid() {
    return copies.stream().filter(Copy::isPaid).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Accède à la liste des exemplaires mis en réservation
   *
   * @return reserved Liste des exemplaires en réservation
   */
  public ArrayList<Copy> getReserved() {
    return copies.stream().filter(Copy::isReserved).collect(Collectors.toCollection(ArrayList::new));
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

    for (Copy copy : copies) {
      if (copy.isAvailable()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  public int quantityAvailable() {
    return getAvailable().size();
  }

  public double amountSold() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isSold()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  public int quantitySold() {
    return getSold().size();
  }

  public double amountPaid() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isPaid()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  public int quantityPaid() {
    return getPaid().size();
  }

  public Comment getComment(int id) {
    for (Comment comment : comments) {
      if (comment.getId() == id) {
        return comment;
      }
    }

    return null;
  }

  public void pay() {
    copies.stream().filter(Copy::isPaid).forEach(copy -> copy.addTransaction("PAY"));
  }

  public void removeCopy(int copyId) {
    for (int i = 0; i < copies.size(); i++) {
      if (copies.get(i).getId() == copyId) {
        copies.remove(i);
        return;
      }
    }
  }

  public void removeCopyReservation(int copyId) {
    for (Reservation r : reservation) {
      if (r.getCopy().getId() == copyId) {
        reservation.remove(r);
        break;
      }
    }
  }

  public void removeItemReservation(int itemId) {
    for (Reservation r : reservation) {
      if (r.getItem().getId() == itemId || r.getCopy().getItem().getId() == itemId) {
        reservation.remove(r);
        break;
      }
    }
  }

  public ArrayList<Reservation> getReservation() {
    return reservation;
  }

  public void fromJSON(JSONObject account) {
    // TODO: Update json filtering
    try {
      if (account.has("registration")) {
        setRegistration(account.getString("registration"));
      }

      if(account.has("last_activity")) {
        setLastActivity(account.getString("last_activity"));
      }

      if (account.has("comment")) {
        JSONArray comments = account.getJSONArray("comment");

        for (int i = 0; i < comments.length(); i++) {
          this.comments.add(new Comment(comments.getJSONObject(i)));
        }
      }

      if (account.has("copies")) {
        JSONArray copies = account.getJSONArray("copies");

        for (int i = 0; i < copies.length(); i++) {
          this.copies.add(new Copy(copies.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JSONArray reservation = account.optJSONArray("reservation");
    if (reservation != null) {
      for (int i = 0; i < reservation.length(); i++) {
        this.reservation.add(new Reservation(reservation.getJSONObject(i)));
      }
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

      for (Copy copy : this.copies) {
        copies.put(copy.toJSON());
      }

      account.put("comment", comments);
      account.put("copies", copies);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return account;
  }

  public JSONObject getStats() {
    JSONObject data;
    JSONObject stats = new JSONObject();

    data = new JSONObject();
    data.put("amount", amountPaid());
    data.put("quantity", quantityPaid());
    stats.put("paid", data);

    data = new JSONObject();
    data.put("amount", amountAvailable());
    data.put("quantity", quantityAvailable());
    stats.put("stock", data);

    data = new JSONObject();
    data.put("amount", amountSold());
    data.put("quantity", quantitySold());
    stats.put("unpaid", data);

    data = new JSONObject();
    data.put("amount", amountSold() + amountPaid());
    data.put("quantity", quantitySold() + quantityPaid());
    stats.put("sold", data);

    data = new JSONObject();
    data.put("amount", amountPaid() + amountAvailable() + amountSold());
    data.put("quantity", copies.size());
    stats.put("total", data);

    return stats;
  }
}
