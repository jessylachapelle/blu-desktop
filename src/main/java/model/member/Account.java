package model.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import model.item.Copy;
import model.item.Reservation;
import utility.DateParser;

/**
 * Le compte d'un member, contient tous les exemplaires mis en vente par le
 * member ainsi que les informations d'acitivité du compte
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 1.1
 */
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
    _init();
  }

  public Account(JSONObject json) {
    _init();
    fromJSON(json);
  }

  /**
   * Constructeur permettant de spécifier les informations de bases du compte
   *
   * @param registration La date d'ouverture du compte
   * @param lastActivity La dernière activité enregistré au compte
   */
  public Account(Date registration, Date lastActivity) {
    _setRegistration(registration);
    setLastActivity(lastActivity);

    comments = new ArrayList<>();
    copies = new ArrayList<>();
  }

  /**
   * Ajoute un comments à la liste
   *
   * @param comment Comment à ajouter
   */
  public void addComment(Comment comment) {
    this.comments.add(comment);
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

  public void editComment(int id, String comment, String updatedBy) {
    for (Comment c : comments) {
      if (c.getId() == id) {
        c.setComment(comment);
        c.setUpdatedBy(updatedBy);
        break;
      }
    }
  }

  public void fromJSON(JSONObject account) {
    String registration = account.optString("registration", "");
    if (!registration.isEmpty()) {
      _setRegistration(registration);
    }

    String lastActivity = account.optString("last_activity", "");
    if (!lastActivity.isEmpty()) {
      _setLastActivity(lastActivity);
    }

    JSONArray comments = account.optJSONArray("comment");
    if (comments != null) {
      this.comments.clear();
      for (int i = 0; i < comments.length(); i++) {
        JSONObject c = comments.optJSONObject(i);
        if (c != null) {
          this.comments.add(new Comment(c));
        }
      }
    }

    JSONArray copies = account.optJSONArray("copies");
    if (copies != null) {
      this.copies.clear();
      for (int i = 0; i < copies.length(); i++) {
        JSONObject c = copies.optJSONObject(i);
        if (c != null) {
          this.copies.add(new Copy(c));
        }
      }
    }

    JSONArray reservations = account.optJSONArray("reservation");
    if (reservations != null) {
      this.reservation.clear();
      for (int i = 0; i < reservations.length(); i++) {
        JSONObject r = reservations.optJSONObject(i);
        if (r != null) {
          this.reservation.add(new Reservation(r));
        }
      }
    }
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
   * Accède à la liste des commentaires associée au compte
   *
   * @return comments La liste des commentaires
   */
  public ArrayList<Comment> getComments() {
    return comments;
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

  public String getDeactivationString() {
    return DateParser.toLongDate(_getDeactivation());
  }

  public String getLastActivityString() {
    return DateParser.toLongDate(lastActivity);
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

  public String getRegistrationString() {
    return DateParser.toLongDate(registration);
  }

  public ArrayList<Reservation> getReservation() {
    return reservation;
  }

  /**
   * Accède à la liste des exemplaires vendus
   *
   * @return sold La liste des exemplaires sold
   */
  public ArrayList<Copy> getSold() {
    return copies.stream().filter(Copy::isSold).collect(Collectors.toCollection(ArrayList::new));
  }

  public JSONObject getStats() {
    JSONObject data;
    JSONObject stats = new JSONObject();

    data = new JSONObject();
    data.put("amount", _amountPaid());
    data.put("quantity", _quantityPaid());
    stats.put("paid", data);

    data = new JSONObject();
    data.put("amount", _amountAvailable());
    data.put("quantity", _quantityAvailable());
    stats.put("stock", data);

    data = new JSONObject();
    data.put("amount", amountSold());
    data.put("quantity", _quantitySold());
    stats.put("unpaid", data);

    data = new JSONObject();
    data.put("amount", amountSold() + _amountPaid());
    data.put("quantity", _quantitySold() + _quantityPaid());
    stats.put("sold", data);

    data = new JSONObject();
    data.put("amount", _amountPaid() + _amountAvailable() + amountSold());
    data.put("quantity", copies.size());
    stats.put("total", data);

    return stats;
  }

  @SuppressWarnings("deprecation")
  public boolean isActive() {
    Date today = new Date();
    Date limit = new Date(today.getYear() - 1, today.getMonth(), today.getDay());
    return lastActivity.after(limit);
  }

  @SuppressWarnings("deprecation")
  public boolean isDeactivated() {
    Date today = new Date();
    Date limit = new Date(today.getYear() - 1, today.getMonth() - 6, today.getDay());
    return lastActivity.before(limit);
  }

  public void pay() {
    copies.stream().filter(Copy::isSold).forEach(copy -> copy.addTransaction("PAY"));
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

  /**
   * Modifie la date de la dernière activité au compte
   *
   * @param lastActivity La date de la dernière activité
   */
  public void setLastActivity(Date lastActivity) {
    this.lastActivity = lastActivity;
  }

  public JSONObject toJSON() {
    JSONObject account = new JSONObject();
    JSONArray comments = new JSONArray();
    JSONArray copies = new JSONArray();

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

    return account;
  }

  private double _amountAvailable() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isAvailable()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  private double _amountPaid() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isPaid()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  @SuppressWarnings("deprecation")
  private Date _getDeactivation() {
    Date deactivation = (Date) _getLastActivity().clone();
    deactivation.setYear(deactivation.getYear() + 1);
    return deactivation;
  }

  /**
   * Accède à la date de la dernière activité au compte
   *
   * @return dateDernièreActivite La date de la dernière activité
   */
  private Date _getLastActivity() {
    return lastActivity;
  }

  private void _init() {
    registration = new Date();
    lastActivity = new Date();
    comments = new ArrayList<>();
    copies = new ArrayList<>();
    reservation = new ArrayList<>();
  }

  private int _quantityAvailable() {
    return getAvailable().size();
  }

  private int _quantityPaid() {
    return getPaid().size();
  }

  private int _quantitySold() {
    return getSold().size();
  }

  /**
   *
   * @param date Date au format aaaa-mm-jj
   */
  private void _setLastActivity(String date) {
    setLastActivity(DateParser.dateFromString(date));
  }

  /**
   * Modifie la date d'ouverture du compte
   *
   * @param registration La date d'ouverture de compte
   */
  private void _setRegistration(Date registration) {
    this.registration = registration;
  }

  /**
   *
   * @param date Date au format aaaa-mm-jj
   */
  private void _setRegistration(String date) {
    _setRegistration(DateParser.dateFromString(date));
  }




}
