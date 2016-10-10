package model.item;

import model.member.Member;
import model.transaction.Transaction;
import java.util.ArrayList;

import model.member.StudentParent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
@SuppressWarnings({"unused"})
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

  public void init() {
    id = 0;
    member = new Member();
    parent = new StudentParent();
    item = new Item();
    price = 0;
    transaction = new ArrayList<>();
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
   * Attribue une valeur au numéro de l'exemplaire
   *
   * @param id Le numéro de l'exemplaire
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Récupère l'item qui est lié à l'exemplaire
   *
   * @return item L'objet item avec les caractéristiques
   */
  public Item getItem() {
    return item;
  }

  /**
   * Attribue un item à l'exemplaire
   *
   * @param item Un objet item avec les caractéristiques
   */
  public void setItem(Item item) {
    this.item = item;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public StudentParent getParent() {
    return (StudentParent)parent;
  }

  public void setParent(Member parent) {
    this.parent = parent;
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

  /**
   * Attribue une valeur au price de l'exemplaire
   *
   * @param price Prix de l'exemplaire
   */
  public void setPrice(double price) {
    this.price = price;
  }

  public String getState() {
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

  public void addTransaction(String type) {
    transaction.add(new Transaction(type));
  }

  /**
   * Ajoute une transaction à l'exemplaire
   *
   * @param transaction Une transaction
   */
  public void addTransaction(Transaction transaction) {
    this.transaction.add(transaction);
  }

  /**
   * Ajoute un array de transaction à l'exemplaire
   *
   * @param transactions Les transactions à ajoutées
   */
  public void addTransactions(ArrayList<Transaction> transactions) {
    this.transaction.addAll(transactions);
  }

  /**
   * Récupère une transaction liée à un exemplaire
   *
   * @param index L'index de la transaction
   * @return transaction La transaction située à l'index
   */
  public Transaction getTransaction(int index) {
    return transaction.get(index);
  }

  /**
   * Récupère toutes les transactions liées à l'exemplaire
   *
   * @return transaction Une liste des transactions
   */
  public ArrayList<Transaction> getAllTransactions() {
    return transaction;
  }

  /**
   * Supprime toutes les transactions liées à un exemplaire
   */
  public void clearTransactions() {
    transaction.clear();
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
   *
   * @return Vrai si l'Copy l'exemplaire est vendu
   */
  public boolean isSold() {
    return isSoldRegular() || isSoldParent();
  }

  public boolean isSoldRegular() {
    return getState().equals("SOLD_REGULAR");
  }

  public boolean isSoldParent() {
    return getState().equals("SOLD_PARENT");
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

  public boolean isAvailable() {
    return getState().equals("ADDED");
  }

  public boolean isPaid() {
    return getState().equals("PAYED");
  }

  private String getDate(String type) {
    for (Transaction t : transaction) {
      if (t.getType().equals(type)) {
        return DateParser.dateToString(t.getDate());
      }
    }
    return "";
  }

  public String getDateAdded() {
    return getDate("ADD");
  }

  public String getDateSold() {
    String date = getDate("SELL");

    if (date.isEmpty()) {
      return getDate("SELL_PARENT");
    }

    return date;
  }

  public String getDatePaid() {
    return getDate("PAY");
  }

  public String getName() {
    return item.getName();
  }

  public String getEdition() {
    if (item instanceof Book) {
      int edition = ((Book) item).getEdition();

      if (edition == 0) {
        return "";
      }

      return Integer.toString(edition);
    }

    return "";
  }

  public String getEditor() {
    if (item instanceof Book) {
      return ((Book) item).getEditor();
    }
    return "";
  }

  public String getSeller() {
    if (member != null) {
      return member.getFirstName() + " " + member.getLastName();
    }

    return "";
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("price")) {
        price = json.getDouble("price");
      }

      if (json.has("member")) {
        member.fromJSON(json.getJSONObject("member"));
      }

      if (json.has("item")) {
        JSONObject itemData = json.getJSONObject("item");

        if (itemData.has("is_book") && itemData.getBoolean("is_book")) {
          item = new Book(itemData);
        } else {
          item = new Item(itemData);
        }
      }

      if (json.has("transaction")) {
        JSONArray transactions = json.getJSONArray("transaction");

        for(int i = 0; i < transactions.length(); i++) {
          transaction.add(new Transaction(transactions.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject copy = new JSONObject();
    JSONArray transaction = new JSONArray();

    try {
      copy.put("id", id);
      copy.put("price", price);
      copy.put("member", member.toJSON());
      copy.put("item", item.toJSON());

      for (Transaction t : this.transaction) {
        transaction.put(t.toJSON());
      }

      copy.put("transaction", transaction);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return copy;
  }

  public String getActivity() {
    if (member != null && member.getAccount().isDeactivated()) {
      return "Désactivé";
    }

    return "";
  }
}
