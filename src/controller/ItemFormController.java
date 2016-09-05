package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.item.Book;
import model.item.Item;

/**
 * Il s'agit du controller responsable de l'ajout d'un item
 * @author Jessy Lachapelle
 * @since 2016/08/01
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class ItemFormController extends Controller {
  @FXML private TabPane tabpane;

  private BookTabController bookTab;
  private ItemTabController itemTab;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    bookTab = new BookTabController();
    itemTab = new ItemTabController();
  }

  public Button getBtnSaveBook() {
    return bookTab.getBtnSave();
  }

  public Button getBtnSaveItem() {
    return itemTab.getBtnSave();
  }

  public Item getItem() {
    if (_isBookTab()) {
      return bookTab.getItem();
    }

    return itemTab.getItem();
  }

  public void loadItem(String ean13) {
    bookTab.loadItem(ean13);
    tabpane.getSelectionModel().select(0);
  }

  public void loadItem(Item item) {
    if (item instanceof  Book) {
      bookTab.loadItem(item);
      tabpane.getSelectionModel().select(0);
    } else {
      itemTab.loadItem(item);
      tabpane.getSelectionModel().select(1);
    }
  }

  public boolean save() {
    if (_isBookTab()) {
      return bookTab.save();
    }

    return itemTab.save();
  }

  private boolean _isBookTab() {
    return tabpane.getSelectionModel().getSelectedItem().getText().equals("Ouvrage");
  }


}
