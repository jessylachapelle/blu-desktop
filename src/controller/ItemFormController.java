package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import model.item.Category;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import handler.ItemHandler;
import model.item.Book;
import model.item.Item;
import model.item.Subject;
import ressources.Dialog;


/**
 * Il s'agit du controller responsable de l'ajout d'un item
 * @author Jessy Lachapelle
 * @since 2016-08-01
 * @version 1.0
 */
public class ItemFormController extends Controller {

  @FXML private TextField txtTitle;
  @FXML private TextField txtEdition;
  @FXML private Button btnRemoveAuthor;
  @FXML private Button btnAddAuthor;
  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;
  @FXML private TextField txtEditor;
  @FXML private TextField txtPublication;
  @FXML private TextField txtEan13;
  @FXML private CheckBox cbHasEan13;
  @FXML private ComboBox<Subject> cbSubject;
  @FXML private Button btnAddBook;

  @FXML private TextField txtItemName;
  @FXML private TextArea txtComment;
  @FXML private ComboBox<Category> cbCategories;
  @FXML private Button btnAddItem;

  private ItemHandler itemHandler;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    itemHandler = new ItemHandler();
    _eventHandlers();
    _initCategoryList();
  }

  /**
   * Initialise notre combobox matière à partir de la bd
   */
  private void _setSubjectList(Category category) {
    ObservableList<Subject> options = FXCollections.observableArrayList(category.getSubjects());

    cbSubject.setItems(options);
    cbSubject.getSelectionModel().select(0);
  }

  /**
   * Initialise notre combobox matière à partir de la bd
   */
  private void _initCategoryList() {
    ObservableList<Category> options = FXCollections.observableArrayList(itemHandler.getCategories());

    cbCategories.setItems(options);
    cbCategories.getSelectionModel().select(0);
    _setSubjectList(cbCategories.getValue());
  }

  public void loadItem(String ean13) {
    txtEan13.setText(ean13);
  }

  // TODO Finir le remplissage des champs
  public void loadItem(Item item) {
    txtEan13.setText(item.getEan13());
  }

  /**
   * Vérifie que les champs nécessaire sont remplit
   * @return vrai si on peut insérer
   */
  private boolean _canAdd(){
    return !txtTitle.getText().isEmpty() && !txtEditor.getText().isEmpty() && !txtPublication.getText().isEmpty() &&
           !cbSubject.getValue().toString().equals("0") && (!txtEan13.getText().isEmpty() || cbHasEan13.selectedProperty().get());
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
      formData.put("subject", cbSubject.getValue().getId());
      formData.put("ean13", txtEan13.getText());

      formData.put("author", authors);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return formData;
  }

  private void _eventHandlers() {
//    // TODO
    btnAddAuthor.setOnAction(event -> {});
//
//    // TODO
    btnRemoveAuthor.setOnAction(event -> {});

    btnAddBook.setOnAction(event -> {
      if (_canAdd()) {
        itemHandler.addItem(getBookData());
      } else {
        Dialog.information("Vous devez remplir tous les champs obligatoires avant d'enregistrer");
      }
    });

    cbCategories.setOnAction(event -> _setSubjectList(cbCategories.getValue()));
  }

  public Item getArticle() {
    return itemHandler.getItem();
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
