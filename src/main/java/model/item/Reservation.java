package model.item;

import java.util.Date;

import org.json.JSONObject;

import model.member.StudentParent;
import utility.DateParser;

/**
 * @author Jessy Lachapelle
 * @since 23/07/16
 * @version 0.1
 */
public class Reservation {
  private int id;
  private StudentParent parent;
  private Date date;
  private Item item;
  private Copy copy;

  public Reservation() {
    _init();
  }

  public Reservation(int id, int memberNo) {
    _init();
    this.id = id;
    parent.setNo(memberNo);
  }

  public Reservation(StudentParent parent, Date date) {
    this.parent = parent;
    this.date = date;
  }

  public Reservation(JSONObject reservation) {
    _init();
    fromJSON(reservation);
  }

  public void fromJSON(JSONObject reservation) {
    if (reservation.has("date")) {
      date = DateParser.dateFromString(reservation.getString("date"));
    }

    JSONObject parent = reservation.optJSONObject("parent");
    if (parent != null) {
      this.parent.fromJSON(parent);
    }

    JSONObject copy = reservation.optJSONObject("copy");
    if (copy != null) {
      this.copy.fromJSON(copy);
    }

    JSONObject item = reservation.optJSONObject("item");
    if (item != null) {
      this.item = item.optBoolean("is_book", false) ? new Book(item) : new Item(item);
    }
  }

  public Copy getCopy() {
    return copy;
  }

  public Date getDate() {
    return date;
  }

  @SuppressWarnings("unused")
  public String getDateAdded() {
    return copy.getDateAdded();
  }

  @SuppressWarnings("unused")
  public String getDateReserved() {
    return DateParser.dateToString(date);
  }

  public int getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }

  @SuppressWarnings("unused")
  public String getItemName() {
    return item.getName();
  }

  @SuppressWarnings("unused")
  public String getMemberName() {
    return copy.getMember().toString();
  }

  public StudentParent getParent() {
    return parent;
  }

  @SuppressWarnings("unused")
  public String getParentName() {
    return parent.toString();
  }

  @SuppressWarnings("unused")
  public String getPrice() {
    return copy.getDateAdded().isEmpty() ? "" : (int) Math.floor(copy.getPrice() / 2) + " $";
  }

  public void setCopy(Copy copy) {
    this.copy = copy;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void setParent(StudentParent parent) {
    this.parent = parent;
  }

  public JSONObject toJSON() {
    JSONObject reservation = new JSONObject();

    reservation.put("parent", parent.toJSON());
    reservation.put("date", DateParser.dateToString(date));
    reservation.put("item", item.toJSON());
    reservation.put("copy", copy.toJSON());

    return reservation;
  }

  private void _init() {
    parent = new StudentParent();
    date = new Date();
    item = new Book();
    copy = new Copy();
  }
}
