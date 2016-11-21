package model.member;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Un member de la BLU, contenant ses coordonnées
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.2
 */
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

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }

  public void fromJSON(JSONObject member) {
    no = member.optInt("no", no);
    firstName = member.optString("first_name", firstName);
    lastName = member.optString("last_name", lastName);
    email = member.optString("email", email);
    address = member.optString("address", address);
    zip = member.optString("zip", zip);


    JSONObject city = member.optJSONObject("city");
    if (city != null) {
      JSONObject state = city.optJSONObject("state");

      this.city = city.optString("name", this.city);
      cityId = city.optInt("id", cityId);
      this.state = state != null ? state.optString("name", this.state) : this.state;
      stateCode = state != null ? state.optString("code", stateCode) : stateCode;
    }

    JSONObject account = member.optJSONObject("account");
    if (account != null) {
      this.account.fromJSON(account);
    }

    JSONArray phones = member.optJSONArray("phone");
    if (phones != null) {
      for(int i = 0; i < phones.length() && i < this.phone.length; i++) {
        JSONObject p = phones.optJSONObject(i);
        if (p != null) {
          phone[i] = new Phone(p);
        }
      }
    }
  }

  /**
   * Accède au nom de la address
   *
   * @return address Le nom de la address
   */
  public String getAddress() {
    return address;
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
   * Accède au account du member
   *
   * @return account Le account du member
   */
  public Account getAccount() {
    return account;
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
   * Accède au prénom du member
   *
   * @return nom Le prénom du member
   */
  public String getFirstName() {
    return firstName;
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
   * Accède au numéro du member
   *
   * @return no Le numéro du member
   */
  public int getNo() {
    return no;
  }

  public Phone[] getPhone() {
    return phone;
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

  public String getStateCode() {
    return stateCode;
  }

  /**
   * Accède au code postal
   *
   * @return zip Le code postal
   */
  public String getZip() {
    return zip;
  }

  public void removePhone(int id) {
    if (phone[1].getId() == id) {
      phone[1] = null;
    } else if (phone[0].getId() == id) {
      phone[0] = phone[1];
      phone[1] = null;
    }
  }

  public void setCityId(int id) {
    cityId = id;
  }

  /**
   * Modifie le numéro du member
   *
   * @param no Le numéro du member
   */
  public void setNo(int no) {
    this.no = no;
  }

  public JSONObject toJSON() {
    JSONObject member = new JSONObject();

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

    return member;
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
}
