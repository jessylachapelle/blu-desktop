package controller;

import handler.ItemHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.item.Category;
import model.item.Item;
import model.item.Subject;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jessy Lachapelle
 * @since 2016-09-04
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemTabController extends Controller {
  private ItemHandler itemHandler = new ItemHandler();

  @FXML private TextField txtName;
  @FXML private ComboBox<Category> cbCategories;
  @FXML private ComboBox<Subject> cbSubjects;
  @FXML private TextArea txtDescription;
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

  public void loadItem(Item item) {
    itemHandler.setItem(item);
    txtName.setText(item.getName());
    txtDescription.setText(item.getDescription());
    _selectCategory();
  }

  public boolean save() {
    return itemHandler.saveItem(toJSON());
  }

  private boolean _canSave() {
    return !txtName.getText().isEmpty();
  }

  public JSONObject toJSON() {
    JSONObject form = new JSONObject();

    try {
      form.put("name", txtName.getText());
      form.put("comment", txtDescription.getText());
      form.put("subject", cbSubjects.getValue().getId());
      form.put("is_book", false);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return form;
  }

  private void _eventHandlers() {
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

    cbSubjects.setItems(options);
    cbSubjects.getSelectionModel().select(subject);
  }
}
