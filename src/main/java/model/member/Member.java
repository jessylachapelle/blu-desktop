package model.member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Un member de la BLU, contenant ses coordonnées
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.2
 */
@SuppressWarnings("unused")
public class Member {
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
   * Constructeur par défaut, crée un member aux valeurs null
   */
  public Member() {
    init();
  }

  public Member(JSONObject json) {
    init();
    fromJSON(json);
  }

  protected void init() {
    no = 0;
    account = new Account();

    phone = new Phone[2];
    phone[0] = new Phone();
    phone[1] = new Phone();

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
   * Accède au numéro du member
   *
   * @return no Le numéro du member
   */
  public int getNo() {
    return no;
  }

  /**
   * Modifie le numéro du member
   *
   * @param no Le numéro du member
   */
  public void setNo(int no) {
    this.no = no;
  }

  /**
   * Accède au account du member
   *
   * @return account Le account du member
   */
  public Account getAccount() {
    return account;
  }

  /**
   * Défini le account du member
   *
   * @param account Le account du member
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

  public void removePhone(int id) {
    if (phone[1].getId() == id) {
      phone[1] = null;
    } else if (phone[0].getId() == id) {
      phone[0] = phone[1];
      phone[1] = null;
    }
  }

  /**
   * Accède au prénom du member
   *
   * @return nom Le prénom du member
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Modifie le prénom du member
   *
   * @param firstName Le prénom du member
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Accède au nom du member
   *
   * @return nom Le nom du member
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Modifie le lastName du member
   *
   * @param lastName Le lastName du member
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Accède au email du member
   *
   * @return email Courriel du member
   */
  public String getEmail() {
    return email;
  }

  /**
   * Modifie le email du member
   *
   * @param email Le email du member
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

  public String getAddressString() {
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
    return zip;
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

  public void fromJSON(JSONObject member) {
    try {
      if (member.has("no")) {
        no = member.getInt("no");
      }

      if (member.has("first_name")) {
        firstName = member.getString("first_name");
      }

      if (member.has("last_name")) {
        lastName = member.getString("last_name");
      }

      if (member.has("email")) {
        email = member.getString("email");
      }

      if (member.has("address")) {
        address = member.getString("address");
      }

      if (member.has("zip")) {
        zip = member.getString("zip");
      }

      if (member.has("city")) {
        JSONObject city = member.getJSONObject("city");
        JSONObject state = city.optJSONObject("state");

        this.city = city.optString("name");
        cityId = city.optInt("id");
        this.state = state != null ? state.optString("name") : "";
        stateCode = state != null ? state.optString("code") : "";
      }

      if (member.has("account")) {
        account.fromJSON(member.getJSONObject("account"));
      }

      if (member.has("phone")) {
        JSONArray phones = member.getJSONArray("phone");

        for(int i = 0; i < phones.length() && i < this.phone.length; i++) {
          phone[i] = new Phone(phones.getJSONObject(i));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public JSONObject toJSON() {
    JSONObject member = new JSONObject();

    try {
      JSONArray phoneArray = new JSONArray();
      for (Phone phone : this.phone) {
        if (!phone.getNumber().isEmpty()) {
          phoneArray.put(phone.toJSON());
        }
      }

      JSONObject city = new JSONObject();
      JSONObject state = new JSONObject();
      state.put("name", this.state);
      state.put("code", stateCode);
      city.put("name", this.city);
      city.put("id", cityId);
      city.put("state", state);

      member.put("no", no);
      member.put("account", account.toJSON());
      member.put("first_name", firstName);
      member.put("last_name", lastName);
      member.put("email", email);
      member.put("address", address);
      member.put("phone", phoneArray);
      member.put("city", city);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return member;
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }
}
