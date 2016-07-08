package handler;

import api.APIConnector;
import bd.AccesBD;
import bd.MembreSQL;
import model.membre.*;
import static bd.MembreSQL.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ressources.JsonParser;

/**
 * Permet de reprendre des ResulSet et de les transformer en objet et de
 * transférer des objets de type Membre à des requêtes de BD
 *
 * @author Jessy Lachapelle
 * @since 29/10/2015
 * @version 0.3
 */
public class MemberHandler {
  private ArrayList<Membre> membres;
  private Membre membre;

  /**
   * Constructeur par défaut
   */
  public MemberHandler() {
    this.membre = new Membre();
    this.membres = new ArrayList<>();
  }

  public Membre getMember(int no) {
    Membre member = new Membre();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "select");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject memberData = APIConnector.call(json).getJSONObject("data");
      member.fromJSON(memberData);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return member;
  }

  /**
   * Search for members with corresponding query
   * @param search The search query
   * @param deactivated True if want to search in deactivated accounts
   * @return A List of members
   */
  public ArrayList<Membre> searchMembers(String search, boolean deactivated) {
    ArrayList<Membre> members = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("search", search);

      if (deactivated) {
        data.put("deactivated", deactivated);
      }

      json.put("function", "search");
      json.put("object", "membre");
      json.put("data", data);

      JSONArray memberArray = APIConnector.call(json).getJSONArray("data");

      for(int i = 0; i < memberArray.length(); i++) {
        members.add(new Membre(memberArray.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return members;
  }

  public Membre insertMember(Membre member) {
    String comment = null;
    if (member.getCompte().getCommentaire().size() > 0) {
      comment = member.getCompte().getCommentaire().get(0).getCommentaire();
    }

    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray phones = new JSONArray();
    JSONObject response;

    try {
      data.append("no", Integer.toString(member.getNoMembre()));
      data.append("prenom", member.getPrenom());
      data.append("nom", member.getNom());
      data.append("no_civic", Integer.toString(member.getNoCivic()));
      data.append("rue", member.getRue());
      data.append("app", member.getAppartement());
      data.append("code_postal", member.getCodePostal());
      data.append("ville", member.getVille());
      data.append("province", member.getProvince());
      data.append("courriel", member.getCourriel());

      if (comment != null) {
        data.append("commentaire", comment);
      }

      for(int i = 0; i < member.getTelephone().length; i++) {
        if (!member.getTelephone(i).getNumero().equals("")) {
          JSONObject phone = new JSONObject();

          phone.append("numero", member.getTelephone(i).getNumero());
          phone.append("note", member.getTelephone(i).getNote());

          phones.put(phone.toString());
        }
      }

      data.append("telephone", phones.toString());
      json.append("object", "membre");
      json.append("function", "insert");
      json.append("data", data.toString());

      response = APIConnector.call(json);

      for(int i = 0; i < member.getTelephone().length; i++) {
        if (!member.getTelephone(i).getNumero().equals("")) {
          int phoneId = response.getInt(member.getTelephone(i).getNumero());
          member.getTelephone(i).setId(phoneId);
        }
      }

      if (comment != null) {
        int commentId = response.getInt("commentaireId");
        member.getCompte().getCommentaire().get(0).setId(commentId);
      }
    } catch (JSONException ex) {
      Logger.getLogger(MemberHandler.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }

    return member;
  }

  public Membre updateMember(Membre member) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONObject jsonMember = new JSONObject();
    JSONArray phones = new JSONArray();
    JSONArray comments = new JSONArray();
    JSONObject response;

    try {
      data.append("noMembre", Integer.toString(member.getNoMembre()));
      jsonMember.append("prenom", member.getPrenom());
      jsonMember.append("nom", member.getNom());
      jsonMember.append("no_civic", Integer.toString(member.getNoCivic()));
      jsonMember.append("rue", member.getRue());
      jsonMember.append("app", member.getAppartement());
      jsonMember.append("code_postal", member.getCodePostal());
      jsonMember.append("ville", member.getVille());
      jsonMember.append("province", member.getProvince());
      jsonMember.append("courriel", member.getCourriel());

      for(int i = 0; i < member.getCompte().getCommentaire().size(); i++) {
        JSONObject comment = new JSONObject();

        comment.append("id", String.valueOf(member.getCompte().getCommentaire().get(i).getId()));
        comment.append("commentaire", member.getCompte().getCommentaire().get(i).getCommentaire());
        comments.put(comment.toString());
      }

      for(int i = 0; i < member.getTelephone().length; i++) {
        if (!member.getTelephone(i).getNumero().equals("")) {
          JSONObject phone = new JSONObject();

          phone.append("numero", member.getTelephone(i).getNumero());
          phone.append("note", member.getTelephone(i).getNote());

          phones.put(phone.toString());
        }
      }

      jsonMember.append("telephone", phones.toString());
      jsonMember.append("commentaires", comments.toString());
      data.append("membre", jsonMember.toString());
      json.append("object", "membre");
      json.append("function", "update");
      json.append("data", data.toString());

      response = APIConnector.call(json);
    } catch (JSONException ex) {
      Logger.getLogger(MemberHandler.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }

    return member;
  }

  public boolean deleteMember(int no) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "delete");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      if (response.has("code")) {
        return response.getInt("code") == 200;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean renewAccount(int no) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", no);
      json.put("function", "renew");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      if (response.has("code")) {
        return response.getInt("code") == 200;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addComment(int noMember, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("no", noMember);
      data.put("comment", comment);

      json.put("function", "insert_comment");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");
      return response.getInt("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return 0;
  }

  public boolean editComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment);

      json.put("function", "update_comment");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean deleteComment(int id) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      json.put("function", "delete_comment");
      json.put("object", "membre");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addTransaction(int no, int id, int type) {
    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(no, id, type).get(0);
    return 0;
  }

  public int addTransaction(int no, int[] copies, int type) {
    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(no, copies, type).get(0);
    return 0;
  }

  public boolean updateCopyPrice(int copyId, double price) {
    CopyHandler copyHandler = new CopyHandler();
    return copyHandler.updateCopyPrice(copyId, price);
  }

  public boolean deleteCopy(int copyId) {
    CopyHandler copyHandler = new CopyHandler();
    return copyHandler.deleteCopy(copyId);
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();
    return transactionHandler.delete(copyId, 2);
  }
























  /**
   * Permet d'ajouter un membre à la BD
   *
   * @param infoMembre Une map de clé/valeur des champs et valeurs à insérer
   * @return vrai si l'ajout a fonctionné
   */
  public boolean ajouteMembre(HashMap infoMembre) {
    return insertMembre(construitMembre(infoMembre));
  }

  /**
   * Permet de supprimer un membre de la BD
   *
   * @param noMembre Le numéro du membre à supprimer
   * @return vrai si la suppression a fonctionné
   */
  public boolean supprimeMembre(int noMembre) {
    return deleteMembre(noMembre);
  }

  /**
   * Permet de tansférer le compte d'un membre vers un autre
   *
   * @param noMembre Le mauvais numéro de membre (à transférer)
   * @return vrai si le transfère a fonctionné
   */
  public boolean transfereMembre(int noMembre) {
    if (transferMembre(membre.getNoMembre(), noMembre)) {
      return supprimeMembre(noMembre);
    }
    return false;
  }

  public boolean ajoutExemplaire(int noMembre, int idArticle, double prix) {
    ItemHandler ga = new ItemHandler();
    TransactionHandler gt = new TransactionHandler();

    int idExemplaire = 0; //ga.ajoutExemplaire(idArticle, prix);
    return gt.ajoutTransaction(1, noMembre, idExemplaire);
  }



  public boolean vendreExemplaire(int noMembre, int idExemplaire) {
    TransactionHandler gt = new TransactionHandler();
    return gt.ajoutTransaction(2, noMembre, idExemplaire);
  }

  public boolean annuleVente(int idExemplaire) {
    TransactionHandler gt = new TransactionHandler();
    return gt.supprimeTransactionVente(idExemplaire);
  }

  public boolean membreExiste(int noMembre) {
    return MembreSQL.membreExiste(noMembre);
  }

  /**
   * Construit un objet de type Membre à partir d'une map clé/valeur
   */
  private Membre construitMembre(ArrayList<HashMap> infoMembres) {
    return construitMembre(infoMembres.get(0));
  }

  /**
   * Construit un objet de type Membre à partir d'une map clé/valeur
   */
  public Membre construitMembre(HashMap<String, String> infoMembre) {
    membre = new Membre();
    membre.setNoMembre(Integer.parseInt(infoMembre.get("no")));
    membre.setPrenom((String) infoMembre.get("prenom"));
    membre.setNom((String) infoMembre.get("nom"));
    membre.setCourriel((String) infoMembre.get("courriel"));
    membre.setNoCivic(Integer.parseInt(infoMembre.get("no_civic")));
    membre.setRue((String) infoMembre.get("rue"));
    membre.setAppartement(infoMembre.get("app").toCharArray());
    membre.setVille((String) infoMembre.get("ville"));
    membre.setProvince((String) infoMembre.get("province"));
    membre.setCodePostal(infoMembre.get("code_postal").toCharArray());

    if (infoMembre.containsKey("numero1")) {
      membre.setPremierTelephone(new Telephone(infoMembre.get("numero1"), infoMembre.get("note1")));
    }

    if (infoMembre.containsKey("numero2")) {
      membre.setSecondTelephone(new Telephone(infoMembre.get("numero2"), infoMembre.get("note2")));
    }

    return membre;
  }

  private void ajoutCompte(int noMembre) {
    String[][] params = {{"noMembre", Integer.toString(noMembre)}};
    membre.setCompte(construitCompte(noMembre, JsonParser.toHashMap(AccesBD.executeFunction("selectCompte", params)).get(0)));
  }

  private Compte construitCompte(int noMembre, HashMap infoCompte) {
    ItemHandler ga = new ItemHandler();
    Compte compte = new Compte();

    Date inscription = new Date();
    String strInscription = (String) infoCompte.get("inscription");
    String anneeInscription = strInscription.substring(0, 4);
    String moisInscription = strInscription.substring(5, 7);
    String jourInscription = strInscription.substring(8, 10);
    inscription.setYear(Integer.parseInt(anneeInscription) - 1900);
    inscription.setMonth(Integer.parseInt(moisInscription) - 1);
    inscription.setDate(Integer.parseInt(jourInscription));

    Date derniereActivite = new Date();
    String strDerniereActivite = (String) infoCompte.get("derniere_activite");
    String anneeDerniereActivite = strDerniereActivite.substring(0, 4);
    String moisDerniereActivite = strDerniereActivite.substring(5, 7);
    String jourDerniereActivite = strDerniereActivite.substring(8, 10);
    derniereActivite.setYear(Integer.parseInt(anneeDerniereActivite) - 1900);
    derniereActivite.setMonth(Integer.parseInt(moisDerniereActivite) - 1);
    derniereActivite.setDate(Integer.parseInt(jourDerniereActivite));

    compte.setDateCreation(inscription);
    compte.setDateDerniereActivite(derniereActivite);
    compte.setSolde(Double.parseDouble((String) infoCompte.get("solde")));

    String[][] params = {{"noMembre", Integer.toString(noMembre)}};
    compte.setCommentaire(construitCommentaire(JsonParser.toHashMap(AccesBD.executeFunction("selectCommentaire", params))));

    compte.setEnVente(ga.consulteExemplairesEnVenteMembre(noMembre));
    compte.setVendu(ga.consulteExemplairesVenduMembre(noMembre));
    compte.setArgentRemis(ga.consulteExemplairesArgentRemisMembre(noMembre));

    return compte;
  }

  /**
   * Construit un objet de type Telephone à partir d'un résultat de requête
   */
  private Telephone construitTelephone(HashMap infoTelephone) {
    Telephone telephone = new Telephone();

    telephone.setNumero((String) infoTelephone.get("numero"));
    telephone.setNote((String) infoTelephone.get("note"));

    return telephone;
  }

  /**
   * Construit un tableau de String à partir d'un résultat de requête
   */
  private ArrayList<Commentaire> construitCommentaire(ArrayList<HashMap> infoCommentaire) {
    ArrayList<Commentaire> commentaires = new ArrayList<>();

    for (int noCommentaire = 0; noCommentaire < infoCommentaire.size(); noCommentaire++) {
      Commentaire c = new Commentaire();

      c.setId(Integer.parseInt((String) infoCommentaire.get(noCommentaire).get("id")));
      c.setCommentaire((String) infoCommentaire.get(noCommentaire).get("commentaire"));
      c.setDate((String) infoCommentaire.get(noCommentaire).get("date_modification"));

      commentaires.add(c);
    }
    return commentaires;
  }
}
