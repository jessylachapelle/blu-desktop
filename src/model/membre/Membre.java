package model.membre;

import model.article.Exemplaire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Un membre de la BLU, contenant ses coordonnées
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.2
 */
public class Membre {
  private int no;
  private Account account;
  private Phone[] phone;
  private String firstName,
                 lastName,
                 email,
                 address,
                 city,
                 state,
                 zip;
  private int cityId;
  private String stateCode;

  /**
   * Constructeur par défaut, crée un membre aux valeurs null
   */
  public Membre() {
    init();
  }

  public Membre(JSONObject json) {
    init();
    fromJSON(json);
  }

  private void init() {
    no = 0;
    account = new Account();
    phone = new Phone[2];
    firstName = "";
    lastName = "";
    email = "";
    address = "";
    city = "";
    state = "";
    zip = "";
    cityId = 0;
    stateCode = "";
  }

  /**
   * Accède au numéro du membre
   *
   * @return no Le numéro du membre
   */
  public int getNo() {
    return no;
  }

  /**
   * Modifie le numéro du membre
   *
   * @param no Le numéro du membre
   */
  public void setNo(int no) {
    this.no = no;
  }

  /**
   * Accède au account du membre
   *
   * @return account Le account du membre
   */
  public Account getAccount() {
    return account;
  }

  /**
   * Défini le account du membre
   *
   * @param account Le account du membre
   */
  public void setAccount(Account account) {
    this.account = account;
  }

  /**
   * Acccède a un téléphone selon un indice
   *
   * @param index doit être 0 ou 1
   * @return le Téléphone
   */
  public Phone getPhone(int index) {
    return phone[index];
  }

  public Phone[] getPhone() {
    return phone;
  }

  public void setPhone(Phone phone) {
    if (this.phone[0] == null) {
      this.phone[0] = phone;
    } else {
      this.phone[1] = phone;
    }
  }

  public void setPhone(Phone phone, int index) {
    if (index >= 0 || index < this.phone.length) {
      this.phone[index] = phone;
    }
  }

  /**
   * Accède au prénom du membre
   *
   * @return nom Le prénom du membre
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Modifie le prénom du membre
   *
   * @param firstName Le prénom du membre
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Accède au nom du membre
   *
   * @return nom Le nom du membre
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Modifie le lastName du membre
   *
   * @param lastName Le lastName du membre
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Accède au email du membre
   *
   * @return email Courriel du membre
   */
  public String getEmail() {
    return email;
  }

  /**
   * Modifie le email du membre
   *
   * @param email Le email du membre
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Accède au nom de la address
   *
   * @return address Le nom de la address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Modifie le nom de la address
   *
   * @param address Le nom de la address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddressStr() {
    return address + ", " + city + " (" + stateCode + ") " + zip;
  }

  /**
   * Accède à la city
   *
   * @return city La city
   */
  public String getCity() {
    return city;
  }

  /**
   * Modifie la city
   *
   * @param city La city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Accède à la state
   *
   * @return state La state
   */
  public String getState() {
    return state;
  }

  /**
   * Modifie la state
   *
   * @param state La state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Accède au code postal
   *
   * @return zip Le code postal
   */
  public String getZip() {
    return String.valueOf(zip);
  }

  /**
   * Modifie le code postal
   *
   * @param zip Le code postal
   */
  public void setZip(String zip) {
    this.zip = zip;
  }

  public void setCityId(int id) {
    cityId = id;
  }

  public void setStateCode(String code) {
    stateCode = code;
  }

  public int getCityId() {
    return cityId;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("no")) {
        no = json.getInt("no");
      }

      if (json.has("first_name")) {
        firstName =json.getString("first_name");
      }

      if (json.has("last_name")) {
        lastName = json.getString("last_name");
      }

      if (json.has("email")) {
        email = json.getString("email");
      }

      if (json.has("address")) {
        address = json.getString("address");
      }

      if (json.has("zip")) {
        zip = json.getString("zip");
      }

      if (json.has("city")) {
        JSONObject city = json.getJSONObject("city");
        JSONObject state = city.getJSONObject("state");

        this.city = city.getString("name");
        cityId = city.getInt("id");
        this.state = state.getString("name");
        stateCode = state.getString("code");
      }

      if (json.has("account")) {
        account.fromJSON(json.getJSONObject("account"));
      }

      if (json.has("phone")) {
        JSONArray phones = json.getJSONArray("phone");

        for(int i = 0; i < phones.length() && i < this.phone.length; i++) {
          phone[i] = new Phone(phones.getJSONObject(i));
        }
      }
    } catch (JSONException e) {}
  }
}
