package model.item;

import model.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Classe item qui contient les informations de base d'un item
 *
 * @author Jessy Lachapelle
 * @since 12/07/2016
 * @version 1.1
 */
@SuppressWarnings("unused")
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

  /**
   * Récupère le numéro de l'item
   *
   * @return id
   */
  public int getId() {
    return id;
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
   * Récupère le name
   *
   * @return name
   */
  public String getName() {
    return name;
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
   * Récupère la subject
   *
   * @return subject
   */
  public Subject getSubject() {
    return subject;
  }

  /**
   * Attribue une valeur à la matière
   *
   * @param subject The item's subject
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
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
   * Attribue une valeur au code barres
   *
   * @param ean13 The item's ean13 code
   */
  public void setEan13(String ean13) {
    this.ean13 = ean13;
  }

  /**
   * Récupère l'unité de rangement
   *
   * @return storage
   */
  public ArrayList<Storage> getStorage() {
    return storage;
  }

  /**
   * Attribue une valeur a l'unité de rangement de l'item
   *
   * @param storage The item's storage information
   */
  public void setStorage(ArrayList<Storage> storage) {
    this.storage = storage;
  }

  public void addStorage(Storage storage) {
    this.storage.add(storage);
  }

  public void addStorage(ArrayList<Storage> storage) {
    this.storage.addAll(storage);
  }

  /**
   * Ajoute une description à l'item
   * @param description A description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Récupère le description lié à un item
   * @return Le description
   */
  public String getDescription() {
    return description;
  }

  public String getStorageString() {
    String storageString = "";

    for (Storage storageUnit : storage) {
      storageString += storageUnit.getCode() + "; ";
    }

    return storageString;
  }

  public void setStorage(String storage) {
    this.storage.clear();
    String[] StorageUnits = storage.split(";");

    for (String storageUnit : StorageUnits) {
      Storage su = new Storage();
      su.setCode(storageUnit);
      this.storage.add(su);
    }
  }

  public void addCopies(ArrayList<Copy> copies) {
    this.copies.addAll(copies);
  }

  private void _addCopy(Copy copy) {
    copies.add(copy);
  }

   /**
   * Accède à la liste des exemplaires en vente
   *
   * @return copies La liste des exemplaires en vente
   */
   private ArrayList<Copy> getCopies() {
    return copies;
  }

  /**
   * Défini la liste des exemplaires en vente
   *
   * @param copies Liste des exemplaires en vente
   */
  public void setCopies(ArrayList<Copy> copies) {
    this.copies = copies;
  }

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

  private double amountAvailable() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isAvailable()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  private int quantityAvailable() {
    return getAvailable().size();
  }

  private double amountSold() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isSold()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  private int quantitySold() {
    return getSold().size();
  }

  private double amountPaid() {
    double amount = 0;

    for (Copy copy : copies) {
      if (copy.isPaid()) {
        amount += copy.getPrice();
      }
    }

    return amount;
  }

  private int quantityPaid() {
    return getPaid().size();
  }

  public void setReservations(ArrayList<Reservation> reservations) {
    this.reservations = reservations;
  }

  public void addReservation(ArrayList<Reservation> reservations) {
    this.reservations.addAll(reservations);
  }

  public void addReservation(Reservation reservation) {
    reservations.add(reservation);
  }

  public Reservation getReservation(int memberNo) {
    for (Reservation reservation : reservations) {
      if (reservation.getParent().getNo() == memberNo) {
        return  reservation;
      }
    }

    return null;
  }

  public void removeReservation(int memberNo) {
    for (Reservation reservation : reservations) {
      if (reservation.getParent().getNo() == memberNo) {
        reservations.remove(reservation);
        break;
      }
    }
  }

  public Copy getCopy(int id) {
    for (Copy copy : copies) {
      if (copy.getId() == id) {
        return copy;
      }
    }

    return null;
  }

  public void removeCopy(int copyId) {
    for (int i = 0; i < copies.size(); i++) {
      if (copies.get(i).getId() == copyId) {
        copies.remove(i);
        return;
      }
    }
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
        this.storage.add(new Storage(storage.getString(i)));
      }
    }

    JSONArray copies = json.optJSONArray("copies");
    if (copies != null) {
      for (int i = 0; i < copies.length(); i++) {
        _addCopy(new Copy(copies.getJSONObject(i)));
      }
    }

    JSONArray reservations = json.optJSONArray("reservation");
    if (reservations != null) {
      for (int i = 0; i < reservations.length(); i++) {
        addReservation(new Reservation(reservations.getJSONObject(i)));
      }
    }
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

  public JSONObject inventory() {
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

  public int maximumPriceTotal() {
    int maximum = 0;

    for (Copy copy : copies) {
      if (copy.getPrice() > maximum) {
        maximum = (int) copy.getPrice();
      }
    }

    return maximum;
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

  public int averagePriceTotal() {
    int total = 0;

    for (Copy copy : copies) {
      total += (int) copy.getPrice();
    }

    return copies.size() == 0 ? 0 : total / copies.size();
  }

  public int averagePriceStock() {
    int total = 0;

    for (Copy copy : getAvailable()) {
      total += (int) copy.getPrice();
    }

    return getAvailable().size() == 0 ? 0 : total / getAvailable().size();
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
}
