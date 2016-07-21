/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
import model.item.Item;
import model.item.Book;
import handler.ItemHandler;
import model.item.Subject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Il s'agit du controller responsable de l'ajout d'un item
 * @author Marc
 */
public class ItemFormController extends Controller {

  private ItemHandler itemHandler;

  private final int MAX_AUTHOR = 5; // le nombre maximale d'auteur que l'ont peut ajouter ou retirer
  private final double ROW_HEIGHT = 30;  // La grandeur d'une row
  private int authorCount;          // L'index auquel le compte d'auteur est rendu
  private Control[][] authorControls;    // Le tableau de controle pour lajout d'auteurs
  private RowConstraints[] authorRow;  // La rangé dans lequel se retrouve les contrôles auteurs
  private BooleanProperty success;

  @FXML private RowConstraints authorRow2;
  @FXML private RowConstraints authorRow3;
  @FXML private RowConstraints authorRow4;
  @FXML private RowConstraints authorRow5;
  @FXML private Button btnRemoveAuthor;
  @FXML private Button btnAddAuthor;
  @FXML private TextField txtf_auteurPrenom_1;
  @FXML private TextField txtf_auteurNom_1;
  @FXML private Label lbl_auteurPrenom_2;
  @FXML private TextField txtf_auteurPrenom_2;
  @FXML private Label lbl_auteurNom_2;
  @FXML private TextField txtf_auteurNom_2;
  @FXML private Label lbl_auteurPrenom_3;
  @FXML private TextField txtf_auteurPrenom_3;
  @FXML private Label lbl_auteurNom_3;
  @FXML private TextField txtf_auteurNom_3;
  @FXML private Label lbl_auteurPrenom_4;
  @FXML private TextField txtf_auteurPrenom_4;
  @FXML private Label lbl_auteurNom_4;
  @FXML private TextField txtf_auteurNom_4;
  @FXML private Label lbl_auteurPrenom_5;
  @FXML private TextField txtf_auteurPrenom_5;
  @FXML private Label lbl_auteurNom_5;
  @FXML private TextField txtf_auteurNom_5;

  @FXML private TextField txtTitle;
  @FXML private TextField txtEdition;
  @FXML private TextField txtEditor;
  @FXML private TextField txtPublication;
  @FXML private TextField txtEan13;
  @FXML private CheckBox cbHasEan13;
  @FXML private ComboBox<KeyValuePair> cbSubject;
  @FXML private Button btnAddBook;
  @FXML private TextField txtItemName;
  @FXML private TextArea txtComment;
  @FXML private ComboBox<?> cbCategories;
  @FXML private Button btnAddItem;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    this.success = new SimpleBooleanProperty(false);
    itemHandler = new ItemHandler();
    _eventHandlers();

    authorCount = 0;
    _initControlArray();
    _initSubjectList();
  }

  /**
   * Initialise notre combobox matière à partir de la bd
   */
  private void _initSubjectList() {
    ObservableList<KeyValuePair> options = FXCollections.observableArrayList(new KeyValuePair("0", "Aucune sélection"));
    ArrayList<Subject> subjects = itemHandler.getSubjects();

    for (Subject subject : subjects) {
      JSONObject json = subject.toJSON();
      int id = 0;
      String name = "";

      try {
        id = json.getInt("id");
        name = json.getString("name");
      } catch (JSONException e) {
        e.printStackTrace();
      }

      options.add(new KeyValuePair(Integer.toString(id), name));
    }

    cbSubject.setItems(options);
    cbSubject.getSelectionModel().select(0);
  }

  public void loadItem(String ean13) {
    txtEan13.setText(ean13);
  }

  // TODO Finir le remplissage des champs
  public void loadItem(Item item) {
    txtEan13.setText(item.getEan13());
  }

  private void _initControlArray() {
    authorRow = new RowConstraints[] { authorRow2, authorRow3, authorRow4, authorRow5 };
    authorControls = new Control[][] {
        {lbl_auteurPrenom_2, txtf_auteurPrenom_2, lbl_auteurNom_2, txtf_auteurNom_2},
        {lbl_auteurPrenom_3, txtf_auteurPrenom_3, lbl_auteurNom_3, txtf_auteurNom_3},
        {lbl_auteurPrenom_4, txtf_auteurPrenom_4, lbl_auteurNom_4, txtf_auteurNom_4},
        {lbl_auteurPrenom_5, txtf_auteurPrenom_5, lbl_auteurNom_5, txtf_auteurNom_5}
    };
  }
  /**
   * Vérifie que les champs nécessaire sont remplit
   * @return vrai si on peut insérer
   */
  private boolean _canAdd(){
    return !txtTitle.getText().isEmpty() && !txtEditor.getText().isEmpty() && !txtPublication.getText().isEmpty() &&
           !cbSubject.getValue().toString().equals("0") && !txtf_auteurNom_1.getText().isEmpty() &&
           (!txtEan13.getText().isEmpty() || cbHasEan13.selectedProperty().get());
  }

  /**
   * Récupere les valeurs des contrôle pour l'ajout
   * @return Un hashmap contenant toute les infos
   */
  private JSONObject getBookData() {
    JSONObject formData = new JSONObject();
    JSONArray authors = new JSONArray();

    try {
      formData.put("is_book", true);
      formData.put("name", txtTitle.getText());
      formData.put("edition", txtEdition.getText());
      formData.put("editor", txtEditor.getText());
      formData.put("publication", txtPublication.getText());
      formData.put("subject", cbSubject.getValue().toString());
      formData.put("ean13", txtEan13.getText());

      if (!txtf_auteurPrenom_1.getText().equals("") && !txtf_auteurNom_1.getText().equals("")) {
        JSONObject author = new JSONObject();
        author.put("first_name", txtf_auteurPrenom_1.getText());
        author.put("last_name", txtf_auteurNom_1.getText());
        authors.put(author);
      }

      if (!txtf_auteurPrenom_2.getText().equals("") && !txtf_auteurNom_2.getText().equals("")) {
        JSONObject author = new JSONObject();
        author.put("first_name", txtf_auteurPrenom_2.getText());
        author.put("last_name", txtf_auteurNom_2.getText());
        authors.put(author);
      }

      if (!txtf_auteurPrenom_3.getText().equals("") && !txtf_auteurNom_3.getText().equals("")) {
        JSONObject author = new JSONObject();
        author.put("first_name", txtf_auteurPrenom_3.getText());
        author.put("last_name", txtf_auteurNom_3.getText());
        authors.put(author);
      }

      if (!txtf_auteurPrenom_4.getText().equals("") && !txtf_auteurNom_4.getText().equals("")) {
        JSONObject author = new JSONObject();
        author.put("first_name", txtf_auteurPrenom_4.getText());
        author.put("last_name", txtf_auteurNom_4.getText());
        authors.put(author);
      }

      if (!txtf_auteurPrenom_5.getText().equals("") && !txtf_auteurNom_5.getText().equals("")) {
        JSONObject author = new JSONObject();
        author.put("first_name", txtf_auteurPrenom_5.getText());
        author.put("last_name", txtf_auteurNom_5.getText());
        authors.put(author);
      }

      formData.put("author", authors);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return formData;
  }

  public BooleanProperty getSuccess() {
    return success;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private void _eventHandlers() {
    btnAddAuthor.setOnAction(event -> {
      if (authorCount < MAX_AUTHOR) { // si on n'a pas atteint le max dauteur de 5
        for (int i = 0; i < 4; i++) {
          authorControls[authorCount][i].setVisible(true);
        }

        authorRow[authorCount].setPrefHeight(ROW_HEIGHT);
        authorCount++;
      }
    });

     btnRemoveAuthor.setOnAction(event -> {
       if (authorCount > 0) { // si on n'a pas atteint le minimum d'auteur
         for (int i = 0; i < 4; i++) {
           authorControls[authorCount -1][i].setVisible(false);
         }

         // on met la valeur des textfield a null
         ((TextField) authorControls[authorCount -1][1]).setText("");
         ((TextField) authorControls[authorCount -1][3]).setText("");

         authorRow[authorCount -1].setPrefHeight(0);
         authorCount--;
       }
     });

    btnAddBook.setOnAction(event -> {
      if (_canAdd()) {
        itemHandler.addItem(getBookData());
        success.set(true);
      } else {
        // TODO: Display a l'utilisateur qu'il manque des info dans un else
      }
    });

  }

  public Item getArticle() {
    return itemHandler.getItem();
  }

  /**
   * Thanks to Hirosh Wickramasuriya
   *
   * http://stackoverflow.com/questions/15554715/how-do-i-populate-a-javafx-choicebox-with-data-from-the-database
   */
  @SuppressWarnings("unused")
  public class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String toString() {
      return value;
    }

    public String getKey() {
      return key;
    }
  }
  public Button getBtnAjoutOuvrage() {
    return btnAddBook;
  }

  public Button getBtnAjoutObjet() {
    return btnAddItem;
  }

  private boolean _isBook() {
    return itemHandler.getItem() instanceof Book;
  }

  public Item addItem() {
    //TODO création de l'item et envoie au serveur
    // return contient l'item au complet incluant le id après insertion
    Item item;

    if (_isBook()) {
      item = new Book();
    } else {
      item = new Item();
    }

    return item;
  }
}
