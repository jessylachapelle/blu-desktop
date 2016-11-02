package model.item;

import model.member.StudentParent;
import org.json.JSONObject;
import utility.DateParser;

import java.util.Date;

/**
 * @author Jessy Lachapelle
 * @since 23/07/16
 * @version 0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
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


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Copy getCopy() {
    return copy;
  }

  public void setCopy(Copy copy) {
    this.copy = copy;
  }

  private void _init() {
    parent = new StudentParent();
    date = new Date();
    item = new Book();
    copy = new Copy();
  }

  public StudentParent getParent() {
    return parent;
  }

  public void setParent(StudentParent parent) {
    this.parent = parent;
  }


  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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
      if (item.optBoolean("is_book", false)) {
        this.item = new Book(item);
      } else {
        this.item = new Item(item);
      }
    }
  }

  public JSONObject toJSON() {
    JSONObject reservation = new JSONObject();

    reservation.put("parent", parent.toJSON());
    reservation.put("date", DateParser.dateToString(date));
    reservation.put("item", item.toJSON());
    reservation.put("copy", copy.toJSON());

    return reservation;
  }

  public String getMemberName() {
    return copy.getMember().toString();
  }

  public String getParentName() {
    return parent.toString();
  }

  public String getPrice() {
    return copy.getDateAdded().isEmpty() ? "" : (int) Math.floor(copy.getPrice() / 2) + " $";
  }

  public String getItemName() {
    return item.getName();
  }

  public String getDateReserved() {
    return DateParser.dateToString(date);
  }

  public String getDateAdded() {
    return copy.getDateAdded();
  }
}
