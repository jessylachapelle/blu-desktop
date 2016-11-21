package model.item;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import model.member.Member;
import model.member.StudentParent;
import model.transaction.Transaction;
import utility.DateParser;

/**
 * Classe exemplaire qui contient les informations spécifiques sur un
 * exemplaire. Un exemplaire est basé sur les informations générales d'un
 * item.
 *
 * @author Jessy Lachapelle
 * @since 13/07/2016
 * @version 1.1
 */
public class Copy {
  private int id;
  private Member member,
                 parent;
  private Item item;
  private double price;
  private ArrayList<Transaction> transaction;

  public Copy() {
    init();
  }

  public Copy(JSONObject json) {
    init();
    fromJSON(json);
  }

  public void addTransaction(String type) {
    transaction.add(new Transaction(type));
  }

  public void fromJSON(JSONObject json) {
    id = json.optInt("id", id);
    price = json.optDouble("price", price);

    JSONObject member = json.optJSONObject("member");
    if (member != null) {
      this.member.fromJSON(member);
    }


    JSONObject item = json.optJSONObject("item");
    if (item != null) {
      this.item = item.optBoolean("is_book", false) ? new Book(item) : new Item(item);
    }

    JSONArray transactions = json.optJSONArray("transaction");
    if (transactions != null) {
      transaction.clear();

      for(int i = 0; i < transactions.length(); i++) {
        JSONObject t = transactions.optJSONObject(i);

        if (t != null) {
          transaction.add(new Transaction(t));
        }
      }
    }
  }

  public String getDateAdded() {
    return _getDate("ADD");
  }

  public String getDatePaid() {
    return _getDate("PAY");
  }

  public String getDateSold() {
    String date = _getDate("SELL");

    if (date.isEmpty()) {
      return _getDate("SELL_PARENT");
    }

    return date;
  }

  @SuppressWarnings("unused")
  public String getEdition() {
    return item instanceof Book ? String.valueOf(((Book) item).getEdition()) : "";
  }

  @SuppressWarnings("unused")
  public String getEditor() {
    return item instanceof Book ? ((Book) item).getEditor() : "";
  }

  /**
   * Récupère la valeur du numéro d'exemplaire
   *
   * @return id Le numéro de l'exemplaire
   */
  public int getId() {
    return id;
  }

  /**
   * Récupère l'item qui est lié à l'exemplaire
   *
   * @return item L'objet item avec les caractéristiques
   */
  public Item getItem() {
    return item;
  }

  public Member getMember() {
    return member;
  }

  public String getName() {
    return item.getName();
  }

  public StudentParent getParent() {
    return (StudentParent)parent;
  }

  /**
   * Récupère le price de l'exemplaire
   *
   * @return price Prix de l'exemplaire
   */
  public double getPrice() {
    return price;
  }

  /**
   * Récupère le price dans un format string
   *
   * @return Le price en string
   */
  public String getPriceString() {
    if (price == 0) {
      return "Gratuit";
    }
    return (int) price + " $";
  }

  public String getSeller() {
    if (isDonated()) {
      return "BLU";
    }

    if (member != null) {
      return member.getFirstName() + " " + member.getLastName();
    }

    return "";
  }

  public Transaction getTransaction(String type) {
    for (Transaction t : transaction) {
      if (t.getType().equals(type)) {
        return t;
      }
    }

    return null;
  }

  public boolean isAvailable() {
    return _getState().equals("ADDED");
  }

  public boolean isDonated() {
    for (Transaction t : transaction) {
      if (t.getType().equals(Transaction.Type.DONATION)) {
        return true;
      }
    }

    return false;
  }

  public boolean isPaid() {
    return _getState().equals("PAYED");
  }

  /**
   *
   * @return Vrai si l'exemplaire est réservé
   */
  public boolean isReserved() {
    for (Transaction t : transaction) {
      if (t.getType().equals("RESERVE")) {
        return true;
      }
    }

    return false;
  }

  /**
   *
   * @return Vrai si l'Copy l'exemplaire est vendu
   */
  public boolean isSold() {
    return _isSoldRegular() || _isSoldParent();
  }

  public void init() {
    id = 0;
    member = new Member();
    parent = new StudentParent();
    item = new Item();
    price = 0;
    transaction = new ArrayList<>();
  }

  public void removeTransaction(String type) {
    for (int i = 0; i < transaction.size(); i++) {
      if (transaction.get(i).getType().equals(type)) {
        transaction.remove(i);
        break;
      }
    }
  }

  /**
   * Attribue une valeur au numéro de l'exemplaire
   *
   * @param id Le numéro de l'exemplaire
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Attribue un item à l'exemplaire
   *
   * @param item Un objet item avec les caractéristiques
   */
  public void setItem(Item item) {
    this.item = item;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public void setParent(Member parent) {
    this.parent = parent;
  }

  /**
   * Attribue une valeur au price de l'exemplaire
   *
   * @param price Prix de l'exemplaire
   */
  public void setPrice(double price) {
    this.price = price;
  }

  public JSONObject toJSON() {
    JSONObject copy = new JSONObject();
    JSONArray transaction = new JSONArray();

    copy.put("id", id);
    copy.put("price", price);
    copy.put("member", member.toJSON());
    copy.put("item", item.toJSON());

    for (Transaction t : this.transaction) {
      transaction.put(t.toJSON());
    }

    copy.put("transaction", transaction);

    return copy;
  }

  private String _getDate(String type) {
    for (Transaction t : transaction) {
      if (t.getType().equals(type)) {
        return DateParser.dateToString(t.getDate());
      }
    }
    return "";
  }

  private String _getState() {
    boolean soldRegular = false,
        soldParent = false,
        payed = false;

    for (Transaction t : transaction) {
      if (t.getType().equals("SELL")) {
        soldRegular = true;
      }

      if (t.getType().equals("SELL_PARENT")) {
        soldParent = true;
      }

      if (t.getType().equals("PAY")) {
        payed = true;
      }
    }

    if (payed) {
      return "PAYED";
    }

    if (soldRegular) {
      return "SOLD_REGULAR";
    }

    if (soldParent) {
      return "SOLD_PARENT";
    }

    return "ADDED";
  }

  private boolean _isSoldRegular() {
    return _getState().equals("SOLD_REGULAR");
  }

  private boolean _isSoldParent() {
    return _getState().equals("SOLD_PARENT");
  }
}
