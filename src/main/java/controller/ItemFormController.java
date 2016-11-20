package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class ItemFormController extends PanelController {
  @FXML private TabPane tabpane;

  private BookTabController bookTab;
  private ItemTabController itemTab;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    bookTab = (BookTabController) _loadPanel("layout/bookTab.fxml", 0);
    itemTab = (ItemTabController) _loadPanel("layout/itemTab.fxml", 1);
  }

  public Button getBtnSaveBook() {
    return bookTab.getBtnSave();
  }

  public Button getBtnSaveItem() {
    return itemTab.getBtnSave();
  }

  public Item getItem() {
    if (_isBookTab()) {
      return bookTab.getBook();
    }

    return itemTab.getItem();
  }

  public void loadItem(String ean13) {
    bookTab.loadItem(ean13);
    tabpane.getSelectionModel().select(0);
  }

  public void loadItem(Item item) {
    if (item instanceof  Book) {
      bookTab.loadItem((Book) item);
      tabpane.getSelectionModel().select(0);
    } else {
      itemTab.loadItem(item);
      tabpane.getSelectionModel().select(1);
    }
  }

  public Button[] getCancelButtons() {
    return new Button[]{bookTab.getBtnCAncel(), itemTab.getBtnCancel()};
  }

  public boolean save() {
    return _isBookTab() ? bookTab.save() : itemTab.save();
  }

  private boolean _isBookTab() {
    return tabpane.getSelectionModel().getSelectedItem().getText().equals("Ouvrage");
  }

  private Controller _loadPanel(String resource, int tabIndex) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(WindowController.class.getClassLoader().getResource(resource));

    try {
      tabpane.getTabs().get(tabIndex).setContent(loader.load());
      return loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  protected void handleScan(String code, boolean isItem) {}
}
