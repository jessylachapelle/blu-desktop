package model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
  private ArrayList<Copy> available,
                          sold,
                          paid,
                          reserved;

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
    available = new ArrayList<>();
    sold = new ArrayList<>();
    paid = new ArrayList<>();
    reserved = new ArrayList<>();
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

  public void setCopies(ArrayList<Copy> copies) {
    copies.forEach(this::addCopy);
  }

  public void addCopy(Copy copy) {
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
  public void removeAvailableCopy(int index) {
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

  public void addPaid(ArrayList<Copy> copies) {
    this.paid.addAll(copies);
  }

  public void addPaid(Copy copy) {
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
   * @param reservationIndex L'exemplaire à remettre en vente
   */
  public void cancelReservation(int reservationIndex) {
    available.add(reserved.remove(reservationIndex));
  }

  /**
   * Prend un exeplaire en réservation et le met dans la liste sold
   *
   * @param reservationIndex L'exemplaire sold
   */
  public void sellReservation(int reservationIndex) {
    sold.add(reserved.remove(reservationIndex));
  }

  /**
   * Prend un exemplaire de available et le met dans sold
   *
   * @param copyIndex Copy sold
   */
  public void sell(int copyIndex) {
    sold.add(available.remove(copyIndex));
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

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("id")) {
        id = json.getInt("id");
      }

      if (json.has("name")) {
        name = json.getString("name");
      }

      if (json.has("subject")) {
        subject.fromJSON(json.getJSONObject("subject"));
      }

      if (json.has("ean13")) {
        ean13 = json.getString("ean13");
      }

      if (json.has("description")) {
        description = json.getString("description");
      }

      if (json.has("storage")) {
        JSONArray storage = json.getJSONArray("storage");

        for (int i = 0; i < storage.length(); i++) {
          this.storage.add(new Storage(storage.getJSONObject(i)));
        }
      }

      if (json.has("copies")) {
        JSONArray copies = json.getJSONArray("copies");

        for (int i = 0; i < copies.length(); i++) {
          addCopy(new Copy(copies.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject item = new JSONObject();
    JSONArray storage = new JSONArray();
    JSONArray copies = new JSONArray();

    try {
      for (Storage storageUnit : this.storage) {
        storage.put(storageUnit.toJSON());
      }

      for (Copy copy : this.available) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : this.sold) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : this.paid) {
        copies.put(copy.toJSON());
      }

      for (Copy copy : this.reserved) {
        copies.put(copy.toJSON());
      }

      item.put("id", id);
      item.put("name", name);
      item.put("subject", subject.toJSON());
      item.put("ean13", ean13);
      item.put("description", description);
      item.put("storage", storage);
      item.put("copies", copies);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return item;
  }
}
