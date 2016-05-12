package hanlder;

import bd.AccesBD;
import bd.ArticleSQL;
import model.article.*;
import static bd.TransactionSQL.*;
import model.transaction.*;
import static bd.ArticleSQL.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

public void GestionnaireArticle() {
    article = null;
    articles = new ArrayList<>();
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
          article.setNom((String) infoArticle.get("ouvrage_titre"));

        if(infoArticle.containsKey("ouvrage_ean13"))
          article.setCodeBar((String) infoArticle.get("ouvrage_ean13"));

        if(infoArticle.containsKey("ouvrage_parution"))
          ((Ouvrage) article).setAnnee(Integer.parseInt((String) infoArticle.get("ouvrage_parution")));

        if(infoArticle.containsKey("editeur_nom"))
          ((Ouvrage) article).setEditeur((String) infoArticle.get("editeur_nom"));

        if(infoArticle.containsKey("ouvrage_no_edition"))
          ((Ouvrage) article).setNoEdition(Integer.parseInt((String) infoArticle.get("ouvrage_no_edition")));

        if(infoArticle.containsKey("ouvrage_date_ajout"))
          ((Ouvrage) article).setDateAjout((String) infoArticle.get("ouvrage_date_ajout"));

        if(infoArticle.containsKey("ouvrage_date_desuet"))
          ((Ouvrage) article).setDateDesuet((String) infoArticle.get("ouvrage_date_desuet"));

        if(infoArticle.containsKey("ouvrage_date_retrait"))
          ((Ouvrage) article).setDateRetire((String) infoArticle.get("ouvrage_date_retrait"));

        for(int noAuteur = 1; noAuteur <= 5; noAuteur++)
          if(infoArticle.containsKey("auteur_" + noAuteur) &&
             !((String)infoArticle.get("auteur_" + noAuteur)).isEmpty())
            ((Ouvrage) article).ajouterAuteur((String) infoArticle.get("auteur_" + noAuteur));
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
      article.setNoArticle(Integer.parseInt(((String) infoArticle.get("id")).replace("\n", "").replace("\r", "")));

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

          return consulterArticle(article.getCodeBar()).getNoArticle();
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
      if(consulterArticle(article.getNom()) == null){
        int succes = insertArticle(article);
        article.setNoArticle(consulterArticle(article.getCodeBar()).getNoArticle());
        if (succes == 1) return article.getNoArticle();
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
    for (int i = 0; i < ((Ouvrage)article).getTousAuteurs().size(); i++){

      HashMap auteur = null;
      //pour chaque auteur, on vérifie s'il y a un nom identique dans la bd
      for (int j = 0; j < 5; j++){
        auteur = selectAuteur(((Ouvrage)article).getAuteur(i), j+1);

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
        if(insertPropriete(idArticle, i+2, ((Ouvrage)article).getAuteur(i)) == false)
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
      editeur = selectEditeur(((Ouvrage)article).getEditeur());

      // si faux, on insere une nouvelle propriete
      if (editeur == null){
        if(insertPropriete(idArticle, 9, ((Ouvrage)article).getEditeur()) == false){
          return OP_ECHEC;
        }
        editeur = selectEditeur(((Ouvrage)article).getEditeur());
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
      annee = selectAnnee(Integer.toString(((Ouvrage)article).getAnnee()));

      // si faux, on insere une nouvelle propriete
      if (annee == null){
        if(insertPropriete(idArticle, 11, (Integer.toString(((Ouvrage)article).getAnnee()))) == false){
          return OP_ECHEC;
        }
        annee = selectAnnee(Integer.toString(((Ouvrage)article).getAnnee()));
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
      edition = selectEdition(Integer.toString(((Ouvrage)article).getNoEdition()));

      // si faux, on insere une nouvelle propriete
      if (edition == null){
        if(insertPropriete(idArticle, 12, (Integer.toString(((Ouvrage)article).getNoEdition()))) == false){
          return OP_ECHEC;
        }
        edition = selectEdition(Integer.toString(((Ouvrage)article).getNoEdition()));
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

  /**
   * Construit une liste d'article à partir de pairs clé-valeur
   *
   * @param mapArticle Une liste de map clé-valeur d'articles
   * @return Une liste d'articles
   */
  private ArrayList<Article> construitListeArticle(ArrayList<HashMap> mapArticle) {
    ArrayList<Article> articles = new ArrayList<>();

    for (int noArticle = 0; noArticle < mapArticle.size(); noArticle++) {
      int id = Integer.parseInt((String) mapArticle.get(noArticle).get("id"));

      if (selectPropriete(id, 1).matches("ouvrage")) {
        Ouvrage ouvrage = new Ouvrage();

        ouvrage.setNoArticle(id);
        ouvrage.setNom((String) mapArticle.get(noArticle).get("nom"));
        ouvrage.setEditeur((String) mapArticle.get(noArticle).get("editeur"));
        ouvrage.setAnnee(Integer.parseInt((String) mapArticle.get(noArticle).get("annee")));
        ouvrage.setNoEdition(Integer.parseInt((String) mapArticle.get(noArticle).get("edition")));

        for (int noAuteur = 1; noAuteur <= 5; noAuteur++) {
          String key = "auteur_" + noAuteur;

          if (mapArticle.get(noArticle).containsKey(key) && mapArticle.get(noArticle).get(key) != null)
            ouvrage.ajouterAuteur((String) mapArticle.get(noArticle).get(key));
        }

        articles.add(ouvrage);
      } else {
        Objet objet = new Objet();

        objet.setNoArticle(id);
        objet.setNom((String) mapArticle.get(noArticle).get("nom"));

        articles.add(objet);
      }
    }

    return articles;
  }

  private ArrayList<Exemplaire> construitListeExemplaireMembre(ArrayList<HashMap> infoExemplaire) {
    ArrayList<Exemplaire> exemplaires = new ArrayList<>();

    for (int noExemplaire = 0; noExemplaire < infoExemplaire.size(); noExemplaire++)
      exemplaires.add(construitExemplaireMembre(infoExemplaire.get(noExemplaire)));
    return exemplaires;
  }

  private Exemplaire construitExemplaireMembre(HashMap infoExemplaire) {
    Exemplaire exemplaire = new Exemplaire();

    exemplaire.setNoExemplaire(Integer.parseInt((String)infoExemplaire.get("exemplaire_id")));
    exemplaire.setPrix(Double.parseDouble((String) infoExemplaire.get("prix")));
    exemplaire.getArticle().setNoArticle(Integer.parseInt((String) infoExemplaire.get("article_id")));
    exemplaire.getArticle().setNom((String) infoExemplaire.get("titre"));

    Date date = new Date();
    String strDate = (String) infoExemplaire.get("date");
    String annee = strDate.substring(0, 4);
    String mois = strDate.substring(5, 7);
    String jour = strDate.substring(8, 10);
    date.setYear(Integer.parseInt(annee) - 1900);
    date.setMonth(Integer.parseInt(mois) - 1);
    date.setDate(Integer.parseInt(jour));

    Transaction transaction = new Transaction();
    transaction.setDate(date);

    exemplaire.ajouterTransaction(transaction);
    return exemplaire;
  }

  private ArrayList<Exemplaire> construitListeExemplaireArticle(ArrayList<HashMap> infoExemplaire) {
    ArrayList<Exemplaire> exemplaires = new ArrayList<>();

    for (int noExemplaire = 0; noExemplaire < infoExemplaire.size(); noExemplaire++)
      exemplaires.add(construitExemplaireArticle(infoExemplaire.get(noExemplaire)));
    return exemplaires;
  }

  private Exemplaire construitExemplaireArticle(HashMap infoExemplaire) {
    Exemplaire exemplaire = new Exemplaire();

    exemplaire.setNoExemplaire(Integer.parseInt((String) infoExemplaire.get("id")));
    exemplaire.setPrix(Double.parseDouble((String) infoExemplaire.get("prix")));
    exemplaire.getMembre().setNoMembre(Integer.parseInt((String) infoExemplaire.get("no")));
    exemplaire.getMembre().setPrenom((String) infoExemplaire.get("prenom"));
    exemplaire.getMembre().setNom((String) infoExemplaire.get("nom"));

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
    exemplaire.getParent().setNoMembre(Integer.parseInt((String)infoDemande.get("no")));
    exemplaire.getParent().setPrenom((String)infoDemande.get("prenom"));
    exemplaire.getParent().setNom((String)infoDemande.get("nom"));

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
   * Consulte une liste d'article selon le nom
   *
   * @param nom Nom de l'article
   * @return Une liste d'article
   */
  public ArrayList<Article> consulterListeArticle(String nom) {
    return construitListeArticle(selectListeArticle(nom));
  }

  /**
   * Construit une liste d'article selon le nom et l'état
   *
   * @param nom Nom de l'article
   * @param etat État de l'article
   * @return Une liste d'article
   */
  public ArrayList<Article> consulterListeArticle(String nom, boolean etat) {
    return construitListeArticle(selectListeArticle(nom, etat));
  }
//
//  /**
//   * Ajoute un article à la base de données
//   *
//   * @param noArticle Le numéro de l'article
//   * @param nom Le nom de l'article
//   * @param etat L'état de l'article
//   * @return Un boolean selon la réussite
//   */
//  private boolean ajoutArticle(int noArticle, String nom, int etat) {
//    return insertArticle(construitArticle(noArticle, nom, etat));
//  }

  /**
   * Supprime un article de la base de données
   *
   * @param noArticle L'article à supprimer
   * @return Vrai si c'est une réussite
   */
  private boolean supprimeArticle(int noArticle) {
    return removeArticle(noArticle);
  }

  /**
   * Modifie l'état d'un article
   *
   * @param noArticle Le numéro de l'article à modifié
   * @param etat Le nouvel état
   * @return Vrai si c'est une réussite
   */
  private boolean modifieEtat(int noArticle, int etat) {
    return updateEtat(noArticle, etat);
  }

  /**
   * Retourne toute les matières
   *
   * @return TOUTE LES MATIÈRES
   */
  public ArrayList<HashMap> getAllMatiere() {
    return selectAllMatiere();
  }

  /**
   * Ajoute une matière à la base de données
   *
   * @param nom Nom de la matière
   * @return Vrai si c'est une réussite
   */
  private boolean ajoutMatiere(String nom) {
    return insertMatiere(nom);
  }

  /**
   * Modifie le nom d'une matière dans la BD
   *
   * @param nouveauNom Le nouveau nom de la matière
   * @param ancienNom Le nom erroné
   * @return Vrai si c'est une réussite
   */
  private boolean modifieMatiere(String nouveauNom, String ancienNom) {
    return updateMatiere(nouveauNom, ancienNom);
  }

  /**
   * Supprime une matière de la base de données
   *
   * @param nom Nom de la matière à supprimée
   * @return Vrai si la matière a été supprimée
   */
  private boolean supprimeMatiere(String nom) {
    return removeMatiere(nom);
  }

  /**
   * Ajoute une catégorie à la base de données
   *
   * @param nom Nom de la catégorie
   * @return Vrai si c'est une réussite
   */
  private boolean ajoutCategorie(String nom) {
    return insertCategorie(nom);
  }

  /**
   * Modifie le nom d'une catégorie dans la BD
   *
   * @param nouveauNom Le nouveau nom de la catégorie
   * @param ancienNom Le nom erroné
   * @return Vrai si c'est une réussite
   */
  private boolean modifieCategorie(String nouveauNom, String ancienNom) {
    return updateCategorie(nouveauNom, ancienNom);
  }

  /**
   * Supprime une catégorie de la base de données
   *
   * @param nom Nom de la catégorie à supprimée
   * @return Vrai si la catégorie a été supprimée
   */
  private boolean supprimeCategorie(String nom) {
    return removeCategorie(nom);
  }

  /**
   * Insère un exemplaire dans la base de données
   *
   * @param noArticle Le numéro de l'article
   * @param prix Le prix de l'exemplaire
   * @return Vrai si l'exemplaire a été ajouté
   */
  private boolean ajoutExemplaire(int noArticle, int prix) {
    return insertExemplaire(noArticle, prix);
  }

  /**
   * Modifie un exemplaire dans la base de données
   *
   * @param noExemplaire Le numéro de l'exemplaire
   * @param prix Le nouveau prix
   * @return Vrai si l'exemplaire a été modifié
   */
  public boolean modifieExemplaire(int noExemplaire, double prix) {
    return updateExemplaire(noExemplaire, prix);
  }

  /**
   * Supprime un exemplaire de la base de données
   *
   * @param noExemplaire L'identifiant de l'exmplaire
   * @return Vrai si l'exemplaire a été supprimé
   */
  public boolean retireExemplaire(int noExemplaire) {
    return removeExemplaire(noExemplaire);
  }

  /**
   * Retire tous les exemplaires liés a un article
   *
   * @param noArticle L'article auquel sont liés les exemplaires
   * @return Vrai si les exemplaires ont été enlevés
   */
  private boolean retireTousExemplaire(int noArticle) {
    return removeTousExemplaire(noArticle);
  }

  /**
   * Vérifie si le type de l'article est ouvrage
   *
   * @param noArticle Le numéro de l'article
   * @param type Le hashmap contenant le type de l'article
   * @return Vrai si l'article est un ouvrage
   */
  private boolean estOuvrage(int noArticle) {
    HashMap type = selectTypeArticle(noArticle);
    String typeArticle = (String) type.get("type_article");

    return "ouvrage".equals(typeArticle);
  }

  /**
   * Ajoute un nouvel auteur si il reste de la place. Un maximum de 5 auteurs
   * peuvent être ajoutés par article.
   *
   * @param noArticle Le numéro de l'article
   * @param nom Le nom de l'auteur
   * @return Vrai si l'auteur a pu etre ajouté.
   */
  private boolean ajoutAuteur(int noArticle, String nom) {
    HashMap hmAuteur = selectAuteur(noArticle, 1);
    String auteur = (String) hmAuteur.get("nom");

    if ("".equals(auteur)) {
      return insertAuteur(noArticle, nom, 1);
    } else {
      if (insertAuteurQuandVide(noArticle, nom, 2) == false) {
        if (insertAuteurQuandVide(noArticle, nom, 3) == false) {
          if (insertAuteurQuandVide(noArticle, nom, 4) == false) {
            if ((insertAuteurQuandVide(noArticle, nom, 5) == false)) {
              return false;
            }
          }
        }
      }
      return true;
    }
  }

  /**
   * Regarde si il reste une place pour ajouter l'auteur. Un maximum de 5
   * auteurs peuvent être ajoutés. Si il reste une place, insère l'auteur dans
   * la BD
   *
   * @param noArticle Le numéro de l'article
   * @param nom Le nom de l'auteur
   * @param numero Le numero d'auteur à tester (1 a 5)
   * @return Vrai si l'auteur a ete ajouté, faux si il n'a pas été ajouté
   */
  private boolean insertAuteurQuandVide(int noArticle, String nom, int numero) {
    HashMap hmAuteur = selectAuteur(noArticle, numero);
    String auteur = (String) hmAuteur.get("nom");

    if ("".equals(auteur)) {
      insertAuteur(noArticle, nom, numero);
      return true;
    }
    return false;
  }

  public boolean ajoutDemandeReservation(int noMembre, int idArticle) {
    return insertDemandeReservation(noMembre, idArticle);
  }

  public boolean supprimeDemandeReservation(int noMembre, int idArticle) {
    return deleteDemandeReservation(noMembre, idArticle);
  }

  /**
   * Insère une réservation pour un exemplaire et un membre dans la BD
   *
   * @param noMembre Le membre qui veut réserver un exemplaire
   * @param idExemplaire Le id de l'exemplaire réservé
   * @return Vrai si la réservation est enregistrée
   */
  public boolean ajoutReservation(int noMembre, int idExemplaire) {
    Date date = new Date();
    Transaction transaction = new Transaction(5, date, idExemplaire, noMembre);

    return insertTransaction(transaction);
  }

  /**
   * Supprime la réservation pour un exemplaire et un membre dans la BD
   * @param idExemplaire Le id de l'exemplaire
   * @return Vrai si la supression est un succès
   */
  public boolean supprimeReservation(int idExemplaire) {
    return deleteReservation(idExemplaire);
  }

  /**
   * Insère un remboursement d'argent selon un exemplaire et un membre dans la
   * BD
   *
   * @param noMembre
   * @param idExemplaire
   * @return
   */
  private boolean rembourseExemplaire(int noMembre, int idExemplaire) {
    Date date = new Date();
    Transaction transaction = new Transaction(3, date, idExemplaire, noMembre);

    return insertTransaction(transaction);
  }

  /**
   * Insère une transaction de mise en vente d'un exemplaire dans la BD
   *
   * @param noMembre Le numero du membre
   * @param idExemplaire Le numero de l'exemplaire associé
   * @return Si la transaction de mise a vente a été inséré
   */
  private boolean miseEnVenteExemplaire(int noMembre, int idExemplaire) {
    Date date = new Date();
    Transaction transaction = new Transaction(1, date, idExemplaire, noMembre);

    return insertTransaction(transaction);
  }

  /**
   * Insère une transaction de vente d'exemplaire dans la BD
   *
   * @param noMembre Le numero du membre
   * @param idExemplaire Le numero de l'exemplaire associé
   * @return Si la transaction a ete inséré
   */
  private boolean venteExemplaire(int noMembre, int idExemplaire) {
    Date date = new Date();
    Transaction transaction = new Transaction(2, date, idExemplaire, noMembre);

    return insertTransaction(transaction);
  }

  public ArrayList<Exemplaire> consulteExemplairesEnVenteMembre(int noMembre) {
    return consulteExemplairesMembre(noMembre, 1);
  }

  public ArrayList<Exemplaire> consulteExemplairesVenduMembre(int noMembre) {
    return consulteExemplairesMembre(noMembre, 2);
  }

  public ArrayList<Exemplaire> consulteExemplairesArgentRemisMembre(int noMembre) {
    return consulteExemplairesMembre(noMembre, 4);
  }

  public ArrayList<Exemplaire> consulteExemplairesMembre(int noMembre, int etat) {
    return construitListeExemplaireMembre(selectExemplairesMembre(noMembre, etat));
  }

  public ArrayList<Exemplaire> consulteExemplairesEnVenteArticle(int idArticle) {
    return consulteExemplairesArticle(idArticle, 1);
  }

  public ArrayList<Exemplaire> consulteExemplairesVenduArticle(int idArticle) {
    return consulteExemplairesArticle(idArticle, 2);
  }

  public ArrayList<Exemplaire> consulteExemplairesArgentRemisArticle(int idArticle) {
    return consulteExemplairesArticle(idArticle, 4);
  }

  public ArrayList<Exemplaire> consulteExemplairesArticle(int idArticle, int etat) {
    return construitListeExemplaireArticle(selectExemplairesArticle(idArticle, etat));
  }

  public ArrayList<Exemplaire> consulteExemplairesArticle(int idArticle) {
    ArrayList<Exemplaire> exemplaires = construitListeExemplaireArticle(selectExemplairesArticle(idArticle));
    exemplaires.addAll(construitDemandeReservation(selectDemandesReservation(idArticle)));

    return exemplaires;
  }

  public ArrayList<UniteRangement> consulteUniteRangement(int idArticle) {
    return construitListeUniteRangement(selectUniteRangement(idArticle));
  }

  private ArrayList<UniteRangement> construitListeUniteRangement(ArrayList<HashMap> infoRangement) {
    ArrayList<UniteRangement> rangement = new ArrayList<>();

    for(int noRangement = 0; noRangement < infoRangement.size(); noRangement++) {
      UniteRangement ur = new UniteRangement();
      ur.setCode((String) infoRangement.get(noRangement).get("valeur"));
      rangement.add(ur);
    }

    return rangement;
  }

  public boolean annuleVente(int idExemplaire) {
    TransactionHalder gt = new TransactionHalder();
    return gt.supprimeTransactionVente(idExemplaire);
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

  public boolean articleExiste(String ean13) {
    return ArticleSQL.articleExiste(ean13);
  }

  public boolean ajoutCommentaire(int idArticle, String commentaire) {
    return ArticleSQL.insertCommentaire(idArticle, commentaire);
  }

  public boolean modifieCommentaire(int idArticle, String commentaire) {
    return ArticleSQL.updateCommentaire(idArticle, commentaire);
  }

  public boolean supprimeCommentaire(int idArticle) {
    return ArticleSQL.deleteCommentaire(idArticle);
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
