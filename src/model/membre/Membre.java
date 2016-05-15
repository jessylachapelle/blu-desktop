package model.membre;

import model.article.Article;
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

  private int noMembre;
  private Compte compte;
  private Telephone[] telephone;
  private String nom[],
                 courriel;

  private int noCivic;
  private String rue;
  private char[] appartement;
  private String ville,
                 province;
  private char[] codePostal;

  /**
   * Constructeur par défaut, crée un membre aux valeurs null
   */
  public Membre() {
    noMembre = 0;
    compte = new Compte();
    telephone = new Telephone[2];
    nom = new String[2];
    nom[0] = "";
    nom[1] = "";
    courriel = "";
    noCivic = 0;
    rue = "";
    appartement = new char[5];
    ville = "";
    province = "";
    codePostal = new char[6];
  }

  public Membre(JSONObject json) {
    noMembre = 0;
    compte = new Compte();
    telephone = new Telephone[2];
    nom = new String[2];
    nom[0] = "";
    nom[1] = "";
    courriel = "";
    noCivic = 0;
    rue = "";
    appartement = new char[5];
    ville = "";
    province = "";
    codePostal = new char[6];
    fromJSON(json);
  }

  /**
   * Accède au numéro du membre
   *
   * @return noMembre Le numéro du membre
   */
  public int getNoMembre() {
    return noMembre;
  }

  /**
   * Modifie le numéro du membre
   *
   * @param noMembre Le numéro du membre
   */
  public void setNoMembre(int noMembre) {
    this.noMembre = noMembre;
  }

  /**
   * Accède au compte du membre
   *
   * @return compte Le compte du membre
   */
  public Compte getCompte() {
    return compte;
  }

  /**
   * Défini le compte du membre
   *
   * @param compte Le compte du membre
   */
  public void setCompte(Compte compte) {
    this.compte = compte;
  }

  /**
   * Acccède a un téléphone selon un indice
   *
   * @param indice doit être 0 ou 1
   * @return le Téléphone
   */
  public Telephone getTelephone(int indice) {
    return telephone[indice];
  }

  public Telephone[] getTelephone() {
    return telephone;
  }

  /**
   * Accède au premier numéro de téléphone
   *
   * @return telephone Le premier numéro de téléphone
   */
  public Telephone getPremierTelephone() {
    return telephone[0];
  }

  /**
   * Accède au second numéro de téléphone
   *
   * @return telephone Le second numéro de téléphone
   */
  public Telephone getSecondTelephone() {
    return telephone[1];
  }

  /**
   * Modifie le premier numéro de téléphone
   *
   * @param telephone Le premier numéro de téléphone
   */
  public void setPremierTelephone(Telephone telephone) {
    this.telephone[0] = telephone;
  }

  /**
   * Modifie le second numéro de téléphone
   *
   * @param telephone Le second numéro de téléphone
   */
  public void setSecondTelephone(Telephone telephone) {
    this.telephone[1] = telephone;
  }

  /**
   * Accède au prénom du membre
   *
   * @return nom Le prénom du membre
   */
  public String getPrenom() {
    return nom[0];
  }

  /**
   * Modifie le prénom du membre
   *
   * @param prenom Le prénom du membre
   */
  public void setPrenom(String prenom) {
    nom[0] = prenom;
  }

  /**
   * Accède au nom du membre
   *
   * @return nom Le nom du membre
   */
  public String getNom() {
    return nom[1];
  }

  /**
   * Modifie le nom du membre
   *
   * @param nom Le nom du membre
   */
  public void setNom(String nom) {
    this.nom[1] = nom;
  }

  /**
   * Accède au courriel du membre
   *
   * @return courriel Courriel du membre
   */
  public String getCourriel() {
    return courriel;
  }

  /**
   * Modifie le courriel du membre
   *
   * @param courriel Le courriel du membre
   */
  public void setCourriel(String courriel) {
    this.courriel = courriel;
  }

  /**
   * Accède au numéro civic
   *
   * @return noCivic Le numéro civic
   */
  public int getNoCivic() {
    return noCivic;
  }

  /**
   * Modifie le numéro civic
   *
   * @param noCivic Le numéro civic
   */
  public void setNoCivic(int noCivic) {
    this.noCivic = noCivic;
  }

  /**
   * Accède au nom de la rue
   *
   * @return rue Le nom de la rue
   */
  public String getRue() {
    return rue;
  }

  /**
   * Modifie le nom de la rue
   *
   * @param rue Le nom de la rue
   */
  public void setRue(String rue) {
    this.rue = rue;
  }

  /**
   * Accède au numéro d'appartement
   *
   * @return appartement Le numéro d'appartement
   */
  public String getAppartement() {
    return String.valueOf(appartement);
  }

  /**
   * Modifie le numéro d'appartement
   *
   * @param appartement Le numéro d'appartement
   */
  public void setAppartement(char[] appartement) {
    this.appartement = appartement;
  }

  /**
   * Accède à la ville
   *
   * @return ville La ville
   */
  public String getVille() {
    return ville;
  }

  /**
   * Modifie la ville
   *
   * @param ville La ville
   */
  public void setVille(String ville) {
    this.ville = ville;
  }

  /**
   * Accède à la province
   *
   * @return province La province
   */
  public String getProvince() {
    return province;
  }

  /**
   * Modifie la province
   *
   * @param province La province
   */
  public void setProvince(String province) {
    this.province = province;
  }

  /**
   * Accède au code postal
   *
   * @return codePostal Le code postal
   */
  public String getCodePostal() {
    return String.valueOf(codePostal);
  }

  /**
   * Modifie le code postal
   *
   * @param codePostal Le code postal
   */
  public void setCodePostal(char[] codePostal) {
    this.codePostal = codePostal;
  }

  public String getAdresse() {
    String cp = String.valueOf(codePostal);
    String adresse = noCivic + ", " + rue;

    if (appartement.length != 0) {
      adresse += " app. " + String.valueOf(appartement);
    }
    adresse += ", " + ville + " (" + province + ")  " + cp.substring(0, 3) + " " + cp.substring(3, 6);

    return adresse;
  }

  public void fromJSON(JSONObject json) {
    try {
      if (json.has("no")) {
        noMembre = json.getInt("no");
      }

      if (json.has("prenom")) {
        setPrenom(json.getString("prenom"));
      }

      if (json.has("nom")) {
        setNom(json.getString("nom"));
      }

      if (json.has("courriel")) {
        courriel = json.getString("courriel");
      }

      if (json.has("exemplaire")) {
        JSONArray copiesData = json.getJSONArray("exemplaire");

        for(int i = 0; i < copiesData.length(); i++) {
          JSONObject copyData = copiesData.getJSONObject(i);
          JSONObject transactions = copyData.getJSONObject("transaction");

          Exemplaire copy = new Exemplaire(copyData);

          if(transactions.has("4")) {
            compte.ajoutArgentRemis(copy);
          } else if(transactions.has("1") && transactions.has("2")) {
            compte.ajoutVendu(copy);
          } else {
            compte.ajoutEnVente(copy);
          }
        }
      }
    } catch (JSONException e) {}
  }
}
