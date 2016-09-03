package model.item;

import model.member.StudentParent;
import org.json.JSONException;
import org.json.JSONObject;
import ressources.DateParser;

import java.util.Date;

/**
 * @author Jessy Lachapelle
 * @since 23/07/16
 * @version 0.1
 */
@SuppressWarnings("unused")
public class Reservation {
  private StudentParent member;
  private Date date;
  private Item item;
  private Copy copy;

  public Reservation() {
    _init();
  }

  public Reservation(int memberNo) {
    _init();
    member.setNo(memberNo);
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

  public Reservation(StudentParent member, Date date) {
    this.member = member;
    this.date = date;
  }

  public Reservation(JSONObject reservation) {
    _init();
    fromJSON(reservation);
  }

  private void _init() {
    member = new StudentParent();
    date = new Date();
    item = new Book();
    copy = new Copy();
  }

  public StudentParent getMember() {
    return member;
  }

  public void setMember(StudentParent member) {
    this.member = member;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void fromJSON(JSONObject reservation) {
    try {
      if (reservation.has("member")) {
        member.fromJSON(reservation.getJSONObject("member"));
      }

      if (reservation.has("date")) {
        date = DateParser.dateFromString(reservation.getString("date"));
      }

      if (reservation.has("copy")) {
        copy.fromJSON(reservation.getJSONObject("copy"));
      }

      if (reservation.has("item")) {
        JSONObject item = reservation.getJSONObject("item");

        if (item.has("is_book") && item.getBoolean("is_book")) {
          this.item = new Book(item);
        } else {
          this.item = new Item(item);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject reservation = new JSONObject();

    try {
      reservation.put("member", member.toJSON());
      reservation.put("date", DateParser.dateToString(date));
      reservation.put("item", item.toJSON());
      reservation.put("copy", copy.toJSON());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return reservation;
  }
}
