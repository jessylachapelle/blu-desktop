package hanlder;

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
  public void GestionnaireMembre() {
    this.membre = new Membre();
    this.membres = new ArrayList<>();
  }

  /**
   * Accède à un membre de la BD
   *
   * @param noMembre Numéro du membre à consulter
   * @return Le membre à consulter
   */
  public Membre consulteMembre(int noMembre) {
    String[][] params = {{"noMembre", Integer.toString(noMembre)}};

    construitMembre(JsonParser.toHashMap(AccesBD.executeFunction("selectMembre", params)));
    ajoutTelephone(noMembre);
    ajoutCompte(noMembre);

    return membre;
  }

  /**
   * Accède à une liste de membre
   *
   * @param nom Le prénom ou nom des membres
   * @return Une liste des membres remplissant le critère
   */
  public ArrayList<Membre> consulteListeMembre(String nom) {
    return construitListeMembre(selectListeMembre(nom));
  }

  /**
   * Accède à une liste de membre
   *
   * @param nom Le prénom ou nom des membres
   * @param desactive Vrai pour obtenir les comptes désactivés
   * @return Une liste des membres remplissant le critère
   */
  public ArrayList<Membre> consulteListeMembre(String nom, boolean desactive) {
    return construitListeMembre(selectListeMembre(nom, desactive));
  }

  /**
   * Accède à une liste de membre
   *
   * @param noMembre Le numéro de membre
   * @return Une liste des membres remplissant le critère
   */
  public ArrayList<Membre> consulteListeMembre(int noMembre) {
    return construitListeMembre(selectListeMembre(noMembre));
  }

  /**
   * Accède à une liste de membre
   *
   * @param noMembre Le numéro de membre
   * @param desactive Vrai pour obtenir les comptes désactivés
   * @return Une liste des membres remplissant le critère
   */
  public ArrayList<Membre> consulteListeMembre(int noMembre, boolean desactive) {
    return construitListeMembre(selectListeMembre(noMembre, desactive));
  }

  /**
   * Permet de modifier un membre dans la BD
   *
   * @param noMembre Le numéro du membre à modifier
   * @param infoMembre Une liste associatives des champs et valeurs à modifier
   * @return vrai si la modification a fonctionné
   */
  public boolean modifieMembre(int noMembre, HashMap infoMembre) {
    return updateMembre(noMembre, infoMembre);
  }

  public int ajoutCommentaire(int noMembre, String commentaire) {
    return insertCommentaire(noMembre, commentaire);
  }

  public boolean modifieCommentaire(int idCommentaire, String commentaire) {
    return updateCommentaire(idCommentaire, commentaire);
  }

  public boolean supprimeCommentaire(int idCommentaire) {
    return deleteCommentaire(idCommentaire);
  }

  public boolean renouveleCompte(int noMembre) {
    String[][] params = {{"noMembre", Integer.toString(noMembre)}};
    return AccesBD.executeUpdate("renouveleCompte", params);
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
    TransactionHalder gt = new TransactionHalder();

    int idExemplaire = 0; //ga.ajoutExemplaire(idArticle, prix);
    return gt.ajoutTransaction(1, noMembre, idExemplaire);
  }

  public boolean supprimeExemplaire(int idExemplaire) {
    ItemHandler ga = new ItemHandler();
    TransactionHalder gt = new TransactionHalder();

    if (gt.supprimeTransactionsExemplaire(idExemplaire)) {
      return ga.retireExemplaire(idExemplaire);
    }
    return false;
  }

  public boolean vendreExemplaire(int noMembre, int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.ajoutTransaction(2, noMembre, idExemplaire);
  }

  public boolean vendreExemplaire(int noMembre, int idExemplaire, boolean rabais) {
    if (rabais) {
      TransactionHalder gt = new TransactionHalder();
      return gt.ajoutTransaction(3, noMembre, idExemplaire);
    }
    return vendreExemplaire(noMembre, idExemplaire);
  }

  public boolean remiseArgentExemplaire(int noMembre, int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.ajoutTransaction(4, noMembre, idExemplaire);
  }

  public boolean reserveExemplaire(int noMembre, int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.ajoutTransaction(5, noMembre, idExemplaire);
  }

  public boolean annuleVente(int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.supprimeTransactionVente(idExemplaire);
  }

  public boolean annuleReservationExemplaire(int noMembre, int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.ajoutTransaction(5, noMembre, idExemplaire);
  }

  public boolean modifiePrixExemplaire(int idExemplaire, double prix) {
    ItemHandler ga = new ItemHandler();
    return ga.modifieExemplaire(idExemplaire, prix);
  }

  public boolean membreExiste(int noMembre) {
    return MembreSQL.membreExiste(noMembre);
  }

  private void ajoutTelephone(int noMembre) {
    String[][] params = {{"noMembre", Integer.toString(noMembre)}};
    ArrayList<HashMap> telephones = JsonParser.toHashMap(AccesBD.executeFunction("selectTelephone", params));

    if (!telephones.isEmpty())
      membre.setPremierTelephone(construitTelephone(telephones.get(0)));
    if (telephones.size() > 1)
      membre.setSecondTelephone(construitTelephone(telephones.get(1)));
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

  /**
   * Construit une liste d'objet de type Membre à partir d'un résultat de
   * requête
   */
  private ArrayList<Membre> construitListeMembre(ArrayList<HashMap> infoMembres) {
    ArrayList<Membre> membres = new ArrayList<>();

    for (int noMembre = 0; noMembre < infoMembres.size(); noMembre++) {
      Membre m = new Membre();

      m.setNoMembre(Integer.parseInt((String) infoMembres.get(noMembre).get("no")));
      m.setPrenom((String) infoMembres.get(noMembre).get("prenom"));
      m.setNom((String) infoMembres.get(noMembre).get("nom"));

      membres.add(m);
    }

    return this.membres = membres;
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

      response = AccesBD.executeFunction(json);

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

      response = AccesBD.executeFunction(json);
    } catch (JSONException ex) {
      Logger.getLogger(MemberHandler.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }

    return member;
  }
}
