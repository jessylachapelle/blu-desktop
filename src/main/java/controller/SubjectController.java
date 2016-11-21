package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import handler.ItemHandler;
import model.item.Category;
import model.item.Item;
import model.item.Subject;

/**
 * Controller to handle Category and Subject comboboxes
 * @author Jessy Lachapelle
 * @since 2016-10-13
 * @version 1.0
 */
public class SubjectController implements Initializable {

  @FXML private ComboBox<Category> cbCategories;
  @FXML private ComboBox<Subject> cbSubject;

  private ItemHandler itemHandler;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public int getSubjectId() {
    return cbSubject.getValue().getId();
  }

  public void init(ItemHandler itemHandler) {
    this.itemHandler = itemHandler;
    _initCategoryList();
    _eventHandlers();
  }

  public void selectCategory() {
    for (Category category : cbCategories.getItems()) {
      if (category.getName().equals(_getItem().getSubject().getCategory().getName())) {
        cbCategories.getSelectionModel().select(category);
        _setSubjectList(category, _getItem().getSubject());
        return;
      }
    }
  }

  private void _eventHandlers() {
    cbCategories.setOnAction(event -> _setSubjectList(cbCategories.getValue()));
  }

  private Item _getItem() {
    return itemHandler.getItem();
  }

  private void _initCategoryList() {
    ObservableList<Category> options = FXCollections.observableArrayList(itemHandler.getCategories());

    cbCategories.setItems(options);
    cbCategories.getSelectionModel().select(0);
    _setSubjectList(cbCategories.getValue());
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