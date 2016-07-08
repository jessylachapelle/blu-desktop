package handler;

import api.APIConnector;
import bd.AccesBD;
import model.article.*;

import model.transaction.*;
import static bd.ArticleSQL.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ressources.JsonParser;

/**
 * Permet de récupérer des informations dans la Base de données et de les
 * transformer en objet. Gère les propriétés des objets et les actions reliées
 * en faisant le lien avec la BD.
 *
 * @author Dereck
 * @version 0.2
 * @since 2015/11/03
 */
public class ItemHandler {

  // Le résulat d'une tentative d'insertion
  private final int OP_ECHEC = 0;
  private final int OP_EXISTE = -1;
  private final int OP_SUCCES = 1;

  private ArrayList<Article> articles;
  private Article article;

  public ItemHandler() {
    article = null;
    articles = new ArrayList<>();
  }

  public boolean updateComment(int id, String comment) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("id", id);
      data.put("comment", comment);

      json.put("function", "update_comment");
      json.put("object", "article");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int addTransaction(int memberNo, int copyId, int type) {
    int id = 0;

    TransactionHandler transactionHandler = new TransactionHandler();
    transactionHandler.insert(memberNo, copyId, type);

    return id;
  }

  public boolean cancelSell(int copyId) {
    TransactionHandler transactionHandler = new TransactionHandler();
    return transactionHandler.delete(copyId, 2);
  }

  public boolean updateStorage(int id, String[] storage) {
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();
    JSONArray storagejson = new JSONArray();

    try {
      for(int i = 0; i < storage.length; i++) {
        storagejson.put(storage[i]);
      }

      data.put("id", id);
      data.put("storage", storagejson);

      json.put("function", "update_storage");
      json.put("object", "article");
      json.put("data", data);

      JSONObject response = APIConnector.call(json).getJSONObject("data");

      return response.has("code") && response.getInt("code") == 200;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }












  public Article getArticle(){
  return this.article;
}
  /**
   * Méthode interne qui construit un objet Article à partir d'une map clé/valeur
   *
   * @param infoArticle La map contenant les clés/valeurs
   * @return article L'objet contenant les informations
   */
  private Article construitArticle(HashMap<String, String> infoArticle) {
    article = null;

    String type = infoArticle.get("type_article");

    switch (type) {
      case "ouvrage":
        article = new Ouvrage();

        if(infoArticle.containsKey("ouvrage_titre"))
          article.setName((String) infoArticle.get("ouvrage_titre"));

        if(infoArticle.containsKey("ouvrage_ean13"))
          article.setCodeBar((String) infoArticle.get("ouvrage_ean13"));

        if(infoArticle.containsKey("ouvrage_parution"))
          ((Ouvrage) article).setPublication(Integer.parseInt((String) infoArticle.get("ouvrage_parution")));

        if(infoArticle.containsKey("editeur_nom"))
          ((Ouvrage) article).setEditor((String) infoArticle.get("editeur_nom"));

        if(infoArticle.containsKey("ouvrage_no_edition"))
          ((Ouvrage) article).setEdition(Integer.parseInt((String) infoArticle.get("ouvrage_no_edition")));

        if(infoArticle.containsKey("ouvrage_date_ajout"))
          ((Ouvrage) article).setDateAjout((String) infoArticle.get("ouvrage_date_ajout"));

        if(infoArticle.containsKey("ouvrage_date_desuet"))
          ((Ouvrage) article).setDateDesuet((String) infoArticle.get("ouvrage_date_desuet"));

        if(infoArticle.containsKey("ouvrage_date_retrait"))
          ((Ouvrage) article).setDateRetire((String) infoArticle.get("ouvrage_date_retrait"));

        for(int noAuteur = 1; noAuteur <= 5; noAuteur++)
          if(infoArticle.containsKey("auteur_" + noAuteur) &&
             !((String)infoArticle.get("auteur_" + noAuteur)).isEmpty())
            ((Ouvrage) article).addAuthor((String) infoArticle.get("auteur_" + noAuteur));
        break;
      case "objet":
        article = new Objet();
        if(infoArticle.containsKey("description"))
          ((Objet) article).setDescription((String) infoArticle.get("description"));
        break;
      default:
        article = new Article();
        break;
    }

    if(infoArticle.containsKey("id"))
      article.setId(Integer.parseInt(((String) infoArticle.get("id")).replace("\n", "").replace("\r", "")));

    if(infoArticle.containsKey("commentaire"))
      article.setCommentaire((String) infoArticle.get("commentaire"));

    if(infoArticle.containsKey("matiere"))
      article.getMatiere().setNom((String) infoArticle.get("matiere"));

    if(infoArticle.containsKey("categorie"))
      article.getMatiere().setCategorie((String) infoArticle.get("categorie"));

    return article;
  }

  /**
   * Méthode qui permet l'ajout d'un nouvelle article
   * @param infoArticle Les informations de l'Article
   * @return Le résultat de l'opération -1 echec, 0 deja dans la bd, sinon l'id de l'insertion
   */
  public int ajouterArticle(HashMap<String, String> infoArticle){
    article = construitArticle(infoArticle);

    if(infoArticle.get("type_article").equals("ouvrage")){
      // On commence par vérifier si l'article est déjà existant dans la base de donnée
      if(!articleExiste(article.getCodeBar())){
        int idArticle = insertArticle(article);

        // si plus grand que 0, on a un id d'article
        if (idArticle > 0 ){
          boolean succes = insertProprieteArticle(idArticle,1); // le type

          if(succes){
            // Si ca fonctionne,
            if (insertToutAuteur(idArticle) == OP_SUCCES){
              // matiere
              if(insertmatiere(idArticle) == OP_SUCCES){
                // editeur
                if(insertEditeur(idArticle) == OP_SUCCES){
                  // annee
                  if(insertAnnee(idArticle) == OP_SUCCES){
                    // edition
                    if(insertEdition(idArticle) == OP_SUCCES){
                      // ean13
                      if(insertEAN13(idArticle) == OP_SUCCES) {
                        // date_ajout
                        if(insertDateAjout(idArticle) == OP_SUCCES){
                          return OP_SUCCES;
                        }
                      }
                    }
                  }
                }
              }
            }
          }

          return consulterArticle(article.getCodeBar()).getId();
        }
       else return OP_ECHEC;
      }
      // le livre existe
      else {
        return OP_EXISTE;
      }
    }
    // Insertion d'un objet
    else {
      if(consulterArticle(article.getName()) == null){
        int succes = insertArticle(article);
        article.setId(consulterArticle(article.getCodeBar()).getId());
        if (succes == 1) return article.getId();
        else return OP_ECHEC;
      }
      // Un objet avec le même nom existe
      else {
        return OP_EXISTE;
      }
    }
  }


  /**
   * Insert tout les auteurs de la liste
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon lid de lauteur qui a flanché
   */
  private int insertToutAuteur(int idArticle) {
    for (int i = 0; i < ((Ouvrage)article).getAuthors().size(); i++){

      HashMap auteur = null;
      //pour chaque auteur, on vérifie s'il y a un nom identique dans la bd
      for (int j = 0; j < 5; j++){
        auteur = selectAuteur(((Ouvrage)article).getAuthor(i), j+1);

        if (auteur != null){
          break;
        }
      }
      // l'auteur existe, donc on ne fait que faire une référence
      if (auteur!= null){
        if(insertProprieteArticle(idArticle, Integer.parseInt((String) auteur.get("id"))) == false)
          return i+1;
      }
      // Nouvelle insertion, puis la référence
      else {
        if(insertPropriete(idArticle, i+2, ((Ouvrage)article).getAuthor(i)) == false)
          return i+1;
      }
    }
    return OP_SUCCES;
  }

    /**
   * Insere une matiere dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertmatiere(int idArticle) {
      HashMap matiere = null;
      // on vérifie s'il y a un nom identique dans la bd
      matiere = selectMatiere(article.getMatiere().getNom());

      // si faux, on insere une nouvelle propriete
      if (matiere == null){
        if(insertPropriete(idArticle, 8, ((Ouvrage)article).getMatiere().getNom()) == false){
          return OP_ECHEC;
        }
        matiere = selectMatiere(article.getMatiere().getNom());
      }
      // La référence
      if(insertProprieteArticle(idArticle,Integer.parseInt((String) matiere.get("id"))) == false)
          return OP_ECHEC;

     return OP_SUCCES;
  }

   /**
   * Insere un editeur dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertEditeur(int idArticle) {
      HashMap editeur;
      // on vérifie s'il y a un nom identique dans la bd
      editeur = selectEditeur(((Ouvrage)article).getEditor());

      // si faux, on insere une nouvelle propriete
      if (editeur == null){
        if(insertPropriete(idArticle, 9, ((Ouvrage)article).getEditor()) == false){
          return OP_ECHEC;
        }
        editeur = selectEditeur(((Ouvrage)article).getEditor());
      }
      // La référence
      if(insertProprieteArticle(idArticle, Integer.parseInt((String) editeur.get("id"))) == false)
          return OP_ECHEC;

     return OP_SUCCES;
  }

   /**
   * Insere une annee dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertAnnee(int idArticle) {
      HashMap annee;
      // on vérifie s'il y a un nom identique dans la bd
      annee = selectAnnee(Integer.toString(((Ouvrage)article).getPublication()));

      // si faux, on insere une nouvelle propriete
      if (annee == null){
        if(insertPropriete(idArticle, 11, (Integer.toString(((Ouvrage)article).getPublication()))) == false){
          return OP_ECHEC;
        }
        annee = selectAnnee(Integer.toString(((Ouvrage)article).getPublication()));
      }

      // La référence
      if(insertProprieteArticle(idArticle, Integer.parseInt((String) annee.get("id"))) == false)
          return OP_ECHEC;

     return OP_SUCCES;
  }

    /**
   * Insere un edition dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertEdition(int idArticle) {
      HashMap edition;
      // on vérifie s'il y a un nom identique dans la bd
      edition = selectEdition(Integer.toString(((Ouvrage)article).getEdition()));

      // si faux, on insere une nouvelle propriete
      if (edition == null){
        if(insertPropriete(idArticle, 12, (Integer.toString(((Ouvrage)article).getEdition()))) == false){
          return OP_ECHEC;
        }
        edition = selectEdition(Integer.toString(((Ouvrage)article).getEdition()));
      }

      // La référence
      if(insertProprieteArticle(idArticle, Integer.parseInt((String) edition.get("id"))) == false)
          return OP_ECHEC;

     return OP_SUCCES;
  }

    /**
   * Insere un code barre dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertEAN13(int idArticle) {
      HashMap ean13 = null;
      // on vérifie s'il y a un nom identique dans la bd
      ean13 = selectEAN13(((Ouvrage)article).getCodeBar());

      // si faux, on insere une nouvelle propriete
      if (ean13 == null){
        if(insertPropriete(idArticle, 13, ((Ouvrage)article).getCodeBar()) == false){
          return OP_ECHEC;
        }
        ean13 = selectEAN13(((Ouvrage)article).getCodeBar());
      }

      // La référence
      if(insertProprieteArticle(idArticle, Integer.parseInt((String) ean13.get("id"))) == false)
          return OP_ECHEC;

     return OP_SUCCES;
  }

  /**
   * Insere la date d'ajout dans la base de donnees
   * @param idArticle l'id de l'article déjà créée
   * @return 1 si succes, sinon 0
   */
  private int insertDateAjout(int idArticle) {
    Date date = new Date();

    if(insertPropriete(idArticle, 14, date.toString()) == false){
      return OP_ECHEC;
    }
    HashMap dateAjout = selectDateAjout(date.toString());

    // La référence
    if(insertProprieteArticle(idArticle, Integer.parseInt((String) dateAjout.get("id"))) == false)
        return OP_ECHEC;

   return OP_SUCCES;
  }

  private ArrayList<Exemplaire> construitListeExemplaireArticle(ArrayList<HashMap> infoExemplaire) {
    ArrayList<Exemplaire> exemplaires = new ArrayList<>();

    for (int noExemplaire = 0; noExemplaire < infoExemplaire.size(); noExemplaire++)
      exemplaires.add(construitExemplaireArticle(infoExemplaire.get(noExemplaire)));
    return exemplaires;
  }

  private Exemplaire construitExemplaireArticle(HashMap infoExemplaire) {
    Exemplaire exemplaire = new Exemplaire();

    exemplaire.setId(Integer.parseInt((String) infoExemplaire.get("id")));
    exemplaire.setPrice(Double.parseDouble((String) infoExemplaire.get("prix")));
    exemplaire.getMembre().setNo(Integer.parseInt((String) infoExemplaire.get("no")));
    exemplaire.getMembre().setFirstName((String) infoExemplaire.get("prenom"));
    exemplaire.getMembre().setLastName((String) infoExemplaire.get("nom"));

    Transaction transaction = new Transaction();
    transaction.setType(1);
    transaction.setDate((String)infoExemplaire.get("date_ajout"));
    exemplaire.ajouterTransaction(transaction);

    if(!((String)infoExemplaire.get("date_vente")).isEmpty()) {
      Transaction t = new Transaction();
      t.setType(2);
      t.setDate((String)infoExemplaire.get("date_vente"));
      exemplaire.ajouterTransaction(t);
    }

    if(!((String)infoExemplaire.get("date_remise")).isEmpty()) {
      Transaction t = new Transaction();
      t.setType(4);
      t.setDate((String)infoExemplaire.get("date_remise"));
      exemplaire.ajouterTransaction(t);
    }

    if(!((String)infoExemplaire.get("date_reservation")).isEmpty()) {
      Transaction t = new Transaction();
      t.setType(5);
      t.setDate((String)infoExemplaire.get("date_reservation"));
      exemplaire.ajouterTransaction(t);
    }

    return exemplaire;
  }

  private ArrayList<Exemplaire> construitDemandeReservation(ArrayList<HashMap> infoDemandes) {
    ArrayList<Exemplaire> exemplaires = new ArrayList<>();

    for(int noDemande = 0; noDemande < infoDemandes.size(); noDemande++)
      exemplaires.add(construitDemandeReservation(infoDemandes.get(noDemande)));
    return exemplaires;
  }

  private Exemplaire construitDemandeReservation(HashMap infoDemande) {
    Exemplaire exemplaire = new Exemplaire();
    Transaction transaction = new Transaction();

    transaction.setType(5);
    transaction.setDate((String)infoDemande.get("date"));

    exemplaire.getTousTransactions().add(transaction);
    exemplaire.getParent().setNo(Integer.parseInt((String)infoDemande.get("no")));
    exemplaire.getParent().setFirstName((String)infoDemande.get("prenom"));
    exemplaire.getParent().setLastName((String)infoDemande.get("nom"));

    return exemplaire;
  }


  /**
   * Accède a un Article et à ses propriétés
   * @param idArticle Le numéro de l'article à consulter
   * @return L'article à consulter
   */
  public Article consulteArticle(int idArticle) {
    String[][] params = {{"idArticle", Integer.toString(idArticle)}};

    construitArticle(JsonParser.toHashMap(AccesBD.executeFunction("selectArticle", params)).get(0));
    article.setExemplaires(consulteExemplairesArticle(idArticle));

    //article.setUniteRangement(consulteUniteRangement(idArticle));

    return article;
  }

  /**
   * Accède a un Article et à ses propriétés
   * @param ean13 Le code à barres associé à l'article
   * @return L'article à consulter
   */
  public Article consulterArticle(String ean13) {
    return consulteArticle(selectIdArticle(ean13));
  }

  /**
   * Search for items with corresponding query
   * @param search The search query
   * @return A list of items
   */
  public ArrayList<Article> searchItem(String search, boolean outdated) {
    ArrayList<Article> items = new ArrayList<>();
    JSONObject json = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("search", search);

      if(outdated) {
        data.put("outdated", outdated);
      }

      json.put("function", "search");
      json.put("object", "article");
      json.put("data", data);

      JSONArray itemArray = APIConnector.call(json).getJSONArray("data");

      for(int i = 0; i < itemArray.length(); i++) {
        JSONObject item = itemArray.getJSONObject(i);

        if (item.has("is_book") && item.getBoolean("is_book")) {
          items.add(new Ouvrage(itemArray.getJSONObject(i)));
        } else {
          items.add(new Objet(itemArray.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return items;
  }

  /**
   * Retourne toute les matières
   *
   * @return TOUTE LES MATIÈRES
   */
  public ArrayList<HashMap> getAllMatiere() {
    return selectAllMatiere();
  }

  public boolean ajoutDemandeReservation(int noMembre, int idArticle) {
    return insertDemandeReservation(noMembre, idArticle);
  }

  public boolean supprimeDemandeReservation(int noMembre, int idArticle) {
    return deleteDemandeReservation(noMembre, idArticle);
  }

  /**
   * Supprime la réservation pour un exemplaire et un membre dans la BD
   * @param idExemplaire Le id de l'exemplaire
   * @return Vrai si la supression est un succès
   */
  public boolean supprimeReservation(int idExemplaire) {
    return deleteReservation(idExemplaire);
  }

  public ArrayList<Exemplaire> consulteExemplairesArticle(int idArticle) {
    ArrayList<Exemplaire> exemplaires = construitListeExemplaireArticle(selectExemplairesArticle(idArticle));
    exemplaires.addAll(construitDemandeReservation(selectDemandesReservation(idArticle)));

    return exemplaires;
  }

  public boolean ajoutDateDesuet(int idArticle) {
    Date date = new Date();
    String strDate = (date.getYear() + 1900) + "/" + (date.getMonth() + 1) + "/" + date.getDate();

    return insertDateDesuet(idArticle, strDate);
  }

  public boolean supprimeDateDesuet(int idArticle) {
    return deleteDateDesuet(idArticle);
  }

  public boolean ajoutDateRetire(int idArticle) {
    Date date = new Date();
    String strDate = (date.getYear() + 1900) + "/" + (date.getMonth() + 1) + "/" + date.getDate();

    return insertDateRetire(idArticle, strDate);
  }

  public boolean supprimeDateRetire(int idArticle) {
    return deleteDateRetire(idArticle);
  }
}
