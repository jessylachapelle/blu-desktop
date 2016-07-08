/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
import model.article.Article;
import model.article.Ouvrage;
import handler.ItemHandler;

/**
 * Il s'agit du controller responsable de l'ajout d'un article
 * @author Marc
 */
public class ItemFormController extends Controller {

  private ItemHandler gArticle;
  @FXML
  private RowConstraints row_auteur_2;
  @FXML
  private RowConstraints row_auteur_3;
  @FXML
  private RowConstraints row_auteur_4;
  @FXML
  private RowConstraints row_auteur_5;
  @FXML
  private TextField ouvrage_titre;
  @FXML
  private TextField ouvrage_no_edition;
  @FXML
  private TextField txtf_auteurPrenom_1;
  @FXML
  private TextField txtf_auteurNom_1;
  @FXML
  private Button btn_retireAuteur;
  @FXML
  private Button btn_ajoutAuteur;
  @FXML
  private Label lbl_auteurPrenom_2;
  @FXML
  private TextField txtf_auteurPrenom_2;
  @FXML
  private Label lbl_auteurNom_2;
  @FXML
  private TextField txtf_auteurNom_2;
  @FXML
  private Label lbl_auteurPrenom_3;
  @FXML
  private TextField txtf_auteurPrenom_3;
  @FXML
  private Label lbl_auteurNom_3;
  @FXML
  private TextField txtf_auteurNom_3;
  @FXML
  private Label lbl_auteurPrenom_4;
  @FXML
  private TextField txtf_auteurPrenom_4;
  @FXML
  private Label lbl_auteurNom_4;
  @FXML
  private TextField txtf_auteurNom_4;
  @FXML
  private Label lbl_auteurPrenom_5;
  @FXML
  private TextField txtf_auteurPrenom_5;
  @FXML
  private Label lbl_auteurNom_5;
  @FXML
  private TextField txtf_auteurNom_5;
  @FXML
  private TextField editeur_nom;
  @FXML
  private TextField ouvrage_parution;
  @FXML
  private TextField ouvrage_ean13;
  @FXML
  private CheckBox cb_code;
  @FXML
  private ComboBox<KeyValuePair> matiere;
  @FXML
  private Button btn_ajoutOuvrage;
  @FXML
  private TextField nom;
  @FXML
  private TextArea commentaire;
  @FXML
  private ComboBox<?> categorie;
  @FXML
  private Button btn_ajoutObjet;

  private final int MAX_AUTEUR = 4; // le nombre maximale d'auteur que l'ont peut ajouter ou retirer
  private final double ROW_HEIGHT = 30;  // La grandeur d'une row
  private int auteurCount;          // L'index auquel le compte d'auteur est rendu
  private Control[][] auteurControles;    // Le tableau de controle pour lajout d'auteurs
  private RowConstraints[] auteurRang;  // La rangé dans lequel se retrouve les contrôles auteurs
  private BooleanProperty success;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
     this.success = new SimpleBooleanProperty(false);
    gArticle = new ItemHandler();
    eventHandlers();

    auteurCount = 0;
    initControlArray();
    initMatiere();
  }

  /**
   * Initialise notre combobox matière à partir de la bd
   */
  private void initMatiere() {

    ObservableList<KeyValuePair> options;
    options = FXCollections.observableArrayList(new KeyValuePair("0", "Aucune sélection"));

    ArrayList<HashMap> allMatiere = gArticle.getAllMatiere();

    for (HashMap<String, String> hashMap : allMatiere) {
       options.add(new KeyValuePair(hashMap.get("id"), hashMap.get("valeur") ));
    }


    matiere.setItems(options);
    matiere.getSelectionModel().select(0);
  }

  public void loadArticle(String code) {
    ouvrage_ean13.setText(code);
  }

  // TODO Finir le remplissage des champs
  public void loadArticle(Article article) {
    ouvrage_ean13.setText(article.getCodeBar());
  }

  private void initControlArray() {
   auteurControles = new Control[][]{
     {lbl_auteurPrenom_2, txtf_auteurPrenom_2, lbl_auteurNom_2, txtf_auteurNom_2},
     {lbl_auteurPrenom_3, txtf_auteurPrenom_3, lbl_auteurNom_3, txtf_auteurNom_3},
     {lbl_auteurPrenom_4, txtf_auteurPrenom_4, lbl_auteurNom_4, txtf_auteurNom_4},
     {lbl_auteurPrenom_5, txtf_auteurPrenom_5, lbl_auteurNom_5, txtf_auteurNom_5},};
   auteurRang = new RowConstraints[]{row_auteur_2, row_auteur_3, row_auteur_4, row_auteur_5};
  }
  /**
   * Vérifie que les champs nécessaire sont remplit
   * @return vrai si on peut insérer
   */
  private boolean peutInserer(){
    if( "".equals(ouvrage_titre.getText()) ||
        "".equals(editeur_nom.getText()) ||
        "".equals(ouvrage_parution.getText()) ||
        matiere.getValue().toString().equals("0") ||
            // TODO fix this
       // ("".equals(ouvrage_ean13.getText()) && cb_code.selectedProperty().get()) ||
        "".equals(txtf_auteurPrenom_1.getText()) ||
        "".equals(txtf_auteurNom_1.getText())){
      return false;
    }
    else
      return true;
  }

  /**
   * Récupere les valeurs des contrôle pour l'ajout
   * @return Un hashmap contenant toute les infos
   */
  private HashMap<String, String> retrieveValues(){
    HashMap<String, String> infoArticle = new HashMap();
    infoArticle.put("type_article", "ouvrage");
    infoArticle.put("ouvrage_titre", ouvrage_titre.getText());
    if(!ouvrage_no_edition.getText().equals(""))
      infoArticle.put("ouvrage_no_edition", ouvrage_no_edition.getText());
    infoArticle.put("editeur_nom", editeur_nom.getText() );
    infoArticle.put("ouvrage_parution", ouvrage_parution.getText());
    infoArticle.put("matiere", matiere.getValue().toString());
    infoArticle.put("ouvrage_ean13", ouvrage_ean13.getText());
    infoArticle.put("auteur_1", txtf_auteurPrenom_1.getText() + " " + txtf_auteurNom_1.getText() );
    if(!txtf_auteurPrenom_2.getText().equals("") && !txtf_auteurNom_2.getText().equals(""))
      infoArticle.put("auteur_2", txtf_auteurPrenom_2.getText() + " " + txtf_auteurNom_2.getText() );
    if(!txtf_auteurPrenom_3.getText().equals("") && !txtf_auteurNom_3.getText().equals(""))
      infoArticle.put("auteur_3", txtf_auteurPrenom_3.getText() + " " + txtf_auteurNom_3.getText() );
    if(!txtf_auteurPrenom_4.getText().equals("") && !txtf_auteurNom_4.getText().equals(""))
      infoArticle.put("auteur_4", txtf_auteurPrenom_4.getText() + " " + txtf_auteurNom_4.getText() );
    if(!txtf_auteurPrenom_5.getText().equals("") && !txtf_auteurNom_5.getText().equals(""))
      infoArticle.put("auteur_5", txtf_auteurPrenom_5.getText() + " " + txtf_auteurNom_5.getText() );
    return infoArticle;
  }

  public BooleanProperty getSuccess() {
    return success;
  }

  private void eventHandlers() {
    btn_ajoutAuteur.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (auteurCount < MAX_AUTEUR) { // si on n'a pas atteint le max dauteur de 5
          for (int i = 0; i < 4; i++) {
            auteurControles[auteurCount][i].setVisible(true);
          }
          auteurRang[auteurCount].setPrefHeight(ROW_HEIGHT);
          auteurCount++;
        }
      }
    });

     btn_retireAuteur.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (auteurCount > 0) { // si on n'a pas atteint le minimum d'auteur
          for (int i = 0; i < 4; i++) {
            auteurControles[auteurCount-1][i].setVisible(false);

          }
          // on met la valeur des textfield a null
          ((TextField)auteurControles[auteurCount-1][1]).setText("");
          ((TextField)auteurControles[auteurCount-1][3]).setText("");

          auteurRang[auteurCount-1].setPrefHeight(0);
          auteurCount--;
        }
      }
    });

    btn_ajoutOuvrage.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {

        if(peutInserer()){
          if(gArticle.ajouterArticle(retrieveValues()) > 0)
            success.set(true);
        }
        // TODO afficher a l'utilisateur qu'il manque des infos dans un else
      }
    });

  }

  public Article getArticle() {
    return gArticle.getArticle();
  }

  /**
   * Thanks to Hirosh Wickramasuriya
   *
   * http://stackoverflow.com/questions/15554715/how-do-i-populate-a-javafx-choicebox-with-data-from-the-database
   */
  public class KeyValuePair {
     private final String key;
     private final String value;

     public KeyValuePair(String key, String value) {
     this.key = key;
     this.value = value;
     }

    public String getKey()   {    return key;    }

    public String toString() {    return value;  }
  }
  public Button getBtnAjoutOuvrage() {
    return btn_ajoutOuvrage;
  }

  public Button getBtnAjoutObjet() {
    return btn_ajoutObjet;
  }

  private boolean estOuvrage() {
    //TODO retour selon la tab sélectionné
    return true;
  }

  public Article ajoutArticle() {
    //TODO création de l'article et envoie au serveur
    // return contient l'article au complet incluant le id après insertion
    Article article;

    if(estOuvrage()) {
      article = new Ouvrage();
    } else {
      article = new Article();
    }

    return article;
  }
}
