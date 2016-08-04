package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
  @FXML private Button btnRemoveAuthor;
  @FXML private Button btnAddAuthor;
  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;

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

  private ItemHandler itemHandler;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    itemHandler = new ItemHandler();
    _eventHandlers();
    _initSubjectList();
  }

  /**
   * Initialise notre combobox matière à partir de la bd
   */
  private void _initSubjectList() {
//    ObservableList<KeyValuePair> options = FXCollections.observableArrayList(new KeyValuePair("0", "Aucune sélection"));
//    ArrayList<Subject> subjects = itemHandler.getSubjects();
//
//    for (Subject subject : subjects) {
//      JSONObject json = subject.toJSON();
//      int id = 0;
//      String name = "";
//
//      try {
//        id = json.getInt("id");
//        name = json.getString("name");
//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
//
//      options.add(new KeyValuePair(Integer.toString(id), name));
//    }
//
//    cbSubject.setItems(options);
//    cbSubject.getSelectionModel().select(0);
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
      formData.put("subject", cbSubject.getValue().toString());
      formData.put("ean13", txtEan13.getText());

      formData.put("author", authors);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return formData;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private void _eventHandlers() {
//    // TODO
//    btnAddAuthor.setOnAction(event -> {});
//
//    // TODO
//    btnRemoveAuthor.setOnAction(event -> {});

//    btnAddBook.setOnAction(event -> {
//      if (_canAdd()) {
//        itemHandler.addItem(getBookData());
//      } else {
//        Dialog.information("Vous devez remplir tous les champs obligatoires avant d'enregistrer");
//      }
//    });
  }

  public Item getArticle() {
    return itemHandler.getItem();
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
