package controller;

import handler.ItemHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.item.Book;
import model.item.Category;
import model.item.Item;
import model.item.Subject;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jessy Lachapelle
 * @since 2016-09-05
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BookTabController extends Controller {
  private ItemHandler itemHandler;

  @FXML private TextField txtTitle;
  @FXML private TextField txtEdition;
  @FXML private TextField txtEditor;
  @FXML private TextField txtPublication;
  @FXML private TextField txtEan13;
  @FXML private CheckBox cbHasEan13;
  @FXML private TextArea txtComment;
  @FXML private ComboBox<Subject> cbSubject;
  @FXML private ComboBox<Category> cbCategories;

  @FXML private Button btnRemoveAuthor;
  @FXML private Button btnAddAuthor;
  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;

  @FXML private Button btnCancel;
  @FXML private Button btnSave;


  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    itemHandler = new ItemHandler();
    _eventHandlers();
    _initCategoryList();
  }

  public Button getBtnSave() {
    return btnSave;
  }

  public Item getItem() {
    return itemHandler.getItem();
  }

  public void loadItem(String ean13) {
    Item item = new Book();
    item.setEan13(ean13);
    itemHandler.setItem(item);
    txtEan13.setText(ean13);
  }

  public void loadItem(Item item) {
    // TODO: Set all fields
    itemHandler.setItem(item);
    txtTitle.setText(item.getName());
    _selectCategory();
  }

  public boolean save() {
    return itemHandler.saveItem(_toJSON());
  }

  private boolean _canSave() {
    // TODO: Check mandatory fields
    return !txtTitle.getText().isEmpty();
  }

  private JSONObject _toJSON() {
    // TODO: Complete field extraction
    JSONObject form = new JSONObject();

    try {
      form.put("name", txtTitle.getText());
      form.put("subject", cbSubject.getValue().getId());
      form.put("is_book", true);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return form;
  }

  private void _eventHandlers() {
    btnCancel.setOnAction(event -> ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(itemHandler.getItem()));

    btnSave.setOnAction(event -> {
      if (_canSave()) {
        if (itemHandler.saveItem(_toJSON())) {
          ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(itemHandler.getItem());
        } else {
          utilitiy.Dialog.information("Une erreur est survenue");
        }
      } else {
        utilitiy.Dialog.information("Veuillez remplir tous les champs obligatoires avant de sauvegarder");
      }
    });

    cbCategories.setOnAction(event -> _setSubjectList(cbCategories.getValue()));
  }

  private void _initCategoryList() {
    ObservableList<Category> options = FXCollections.observableArrayList(itemHandler.getCategories());

    cbCategories.setItems(options);
    cbCategories.getSelectionModel().select(0);
    _setSubjectList(cbCategories.getValue());
  }

  private void _selectCategory() {
    for (Category category : cbCategories.getItems()) {
      if (category.getName().equals(getItem().getSubject().getCategory().getName())) {
        cbCategories.getSelectionModel().select(category);
        _setSubjectList(category, getItem().getSubject());
        return;
      }
    }
  }

  private void _setSubjectList(Category category) {
    _setSubjectList(category, category.getSubjects().get(0));
  }

  private void _setSubjectList(Category category, Subject subject) {
    ObservableList<Subject> options = FXCollections.observableArrayList(category.getSubjects());

    cbSubject.setItems(options);
    cbSubject.getSelectionModel().select(subject);
  }
}
