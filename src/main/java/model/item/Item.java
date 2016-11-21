package model.item;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import model.transaction.Transaction;

/**
 * Classe item qui contient les informations de base d'un item
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 * @version 1.1
 */
public class Item {
  private int id;
  private String name;
  private Subject subject;
  private String ean13;
  private ArrayList<Storage> storage;
  private String description;
  private ArrayList<Copy> copies;
  private ArrayList<Reservation> reservations;
  /**
   * Constructeur sans paramètre de la classe Item
   */
  public Item() {
    init();
  }

  public Item(JSONObject json) {
    init();
    fromJSON(json);
  }

  public void addReservation(Reservation reservation) {
    reservations.add(reservation);
  }

  public int averagePriceStock() {
    int total = 0;

    for (Copy copy : getAvailable()) {
      total += (int) copy.getPrice();
    }

    return getAvailable().size() == 0 ? 0 : total / getAvailable().size();
  }

  public int averagePriceTotal() {
    int total = 0;

    for (Copy copy : copies) {
      total += (int) copy.getPrice();
    }

    return copies.size() == 0 ? 0 : total / copies.size();
  }

  public void fromJSON(JSONObject json) {
    id = json.optInt("id", id);
    name = json.optString("name", name);
    ean13 = json.optString("ean13", ean13);
    description = json.optString("comment", description);

    JSONObject subject = json.optJSONObject("subject");
    if (subject != null) {
      this.subject.fromJSON(json.getJSONObject("subject"));
    }

    JSONArray storage = json.optJSONArray("storage");
    if (storage != null) {
      for (int i = 0; i < storage.length(); i++) {
        String s = storage.optString(i, "");
        if (!s.isEmpty()) {
          this.storage.add(new Storage(s));
        }
      }
    }

    JSONArray copies = json.optJSONArray("copies");
    if (copies != null) {
      for (int i = 0; i < copies.length(); i++) {
        JSONObject copy = copies.optJSONObject(i);
        if (copy != null) {
          _addCopy(new Copy(copy));
        }
      }
    }

    JSONArray reservations = json.optJSONArray("reservation");
    if (reservations != null) {
      for (int i = 0; i < reservations.length(); i++) {
        JSONObject reservation = reservations.optJSONObject(i);
        if (reservation != null) {
          addReservation(new Reservation(reservation));
        }
      }
    }
  }

  public ArrayList<Copy> getAvailable() {
    return copies.stream().filter(Copy::isAvailable).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Accède à la liste des exemplaires en vente
   *
   * @return copies La liste des exemplaires en vente
   */
  private ArrayList<Copy> getCopies() {
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
   * Récupère le description lié à un item
   * @return Le description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Récupère le code barres
   *
   * @return ean13
   */
  public String getEan13() {
    return ean13;
  }

  /**
   * Récupère le numéro de l'item
   *
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Récupère le name
   *
   * @return name
   */
  public String getName() {
    return name;
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
  public ArrayList<Reservation> getReserved() {
    ArrayList<Reservation> reservations = new ArrayList<>();
    ArrayList<Copy> copies = this.copies.stream().filter(Copy::isReserved).collect(Collectors.toCollection(ArrayList::new));

    reservations.addAll(this.reservations);

    for (Copy copy : copies) {
      Reservation reservation = new Reservation();
      Transaction transaction = copy.getTransaction("RESERVE");

      if (transaction != null) {
        reservation.setDate(transaction.getDate());
        reservation.setParent(transaction.getParent());
        reservation.setCopy(copy);
        reservation.setItem(copy.getItem());

        reservations.add(reservation);
      }
    }

    return reservations;
  }

  /**
   * Accède à la liste des exemplaires vendus
   *
   * @return sold La liste des exemplaires sold
   */
  public ArrayList<Copy> getSold() {
    return copies.stream().filter(Copy::isSold).collect(Collectors.toCollection(ArrayList::new));
  }

  public String getStorageString() {
    String storageString = "";

    for (Storage storageUnit : storage) {
      storageString += storageUnit.getCode() + "; ";
    }

    return storageString;
  }

  /**
   * Récupère la subject
   *
   * @return subject
   */
  public Subject getSubject() {
    return subject;
  }

  public void init() {
    id = 0;
    name = "";
    subject = new Subject();
    ean13 = "";
    storage = new ArrayList<>();
    description = "";
    copies = new ArrayList<>();
    reservations = new ArrayList<>();
  }

  public JSONObject inventory() {
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
    data.put("amount", _amountSold());
    data.put("quantity", _quantitySold());
    stats.put("unpaid", data);

    data = new JSONObject();
    data.put("amount", _amountSold() + _amountPaid());
    data.put("quantity", _quantitySold() + _quantityPaid());
    stats.put("sold", data);

    data = new JSONObject();
    data.put("amount", _amountPaid() + _amountAvailable() + _amountSold());
    data.put("quantity", copies.size());
    stats.put("total", data);

    return stats;
  }

  public int maximumPriceStock() {
    int maximum = 0;

    for (Copy copy : getAvailable()) {
      if (copy.getPrice() > maximum) {
        maximum = (int) copy.getPrice();
      }
    }

    return maximum;
  }

  public int maximumPriceTotal() {
    int maximum = 0;

    for (Copy copy : copies) {
      if (copy.getPrice() > maximum) {
        maximum = (int) copy.getPrice();
      }
    }

    return maximum;
  }

  public int minimumPriceStock() {
    ArrayList<Copy> copies = getAvailable();
    int minimum = copies.size() > 0 ? (int) copies.get(0).getPrice() : 0;

    for (int i = 1; i < copies.size(); i++) {
      if (copies.get(i).getPrice() < minimum) {
        minimum = (int) copies.get(i).getPrice();
      }
    }

    return minimum;
  }

  public int minimumPriceTotal() {
    ArrayList<Copy> copies = getCopies();
    int minimum = copies.size() > 0 ? (int) copies.get(0).getPrice() : 0;

    for (int i = 1; i < copies.size(); i++) {
      if (copies.get(i).getPrice() < minimum) {
        minimum = (int) copies.get(i).getPrice();
      }
    }

    return minimum;
  }

  public void removeCopy(int copyId) {
    for (int i = 0; i < copies.size(); i++) {
      if (copies.get(i).getId() == copyId) {
        copies.remove(i);
        return;
      }
    }
  }

  public void removeReservation(int memberNo) {
    for (Reservation reservation : reservations) {
      if (reservation.getParent().getNo() == memberNo) {
        reservations.remove(reservation);
        break;
      }
    }
  }

  /**
   * Ajoute une description à l'item
   * @param description A description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Attribue une valeur au code barres
   *
   * @param ean13 The item's ean13 code
   */
  public void setEan13(String ean13) {
    this.ean13 = ean13;
  }

  /**
   * Attribue une valeur au numéro d'item
   *
   * @param id The item's id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Attribue une valeur au name d'item
   *
   * @param name The item's name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Attribue une valeur a l'unité de rangement de l'item
   *
   * @param storage The item's storage information
   */
  public void setStorage(ArrayList<Storage> storage) {
    this.storage = storage;
  }

  public JSONObject toJSON() {
    JSONObject item = new JSONObject();
    JSONArray storage = new JSONArray();
    JSONArray copies = new JSONArray();
    JSONArray reservations = new JSONArray();

    for (Storage storageUnit : this.storage) {
      storage.put(storageUnit.toJSON());
    }

    for (Copy copy : this.copies) {
      copies.put(copy.toJSON());
    }

    for (Reservation reservation : this.reservations) {
      reservations.put(reservation.toJSON());
    }

    item.put("id", id);
    item.put("name", name);
    item.put("subject", subject.toJSON());
    item.put("ean13", ean13);
    item.put("description", description);
    item.put("storage", storage);
    item.put("copies", copies);
    item.put("reservation", reservations);

    return item;
  }

  private void _addCopy(Copy copy) {
    copies.add(copy);
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

  private double _amountSold() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isSold()) {
        amount += copy.getPrice();
      }
    }

    return amount;
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
}
