package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import handler.ItemHandler;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.scene.layout.VBox;
import model.item.Copy;
import model.item.Item;
import handler.CopyHandler;
import model.member.Member;

import utility.Dialog;

/**
 * Controller de l'interface d'ajout
 * d'copies dans un compte de member
 * gère aussi la recherche et l'ajout d'articles
 * @author Jessy
 * @since 28/03/2016
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class CopyFormController extends PanelController {
  private Controller controller;

  private CopyHandler copyHandler;
  private Member member;
  private ArrayList<Copy> copies;
  private Copy currentCopy;

  @FXML private Pane resources;
  @FXML private VBox setPrice;
  @FXML private Label memberName;
  @FXML private Label itemTitle;
  @FXML private Button btnCancel;
  @FXML private Button btnAdd;
  @FXML private TextField txtPrice;
  @FXML private TableView<Copy> tblCopies;
  @FXML private TableColumn<Copy, String> colItem;
  @FXML private TableColumn<Copy, Double> colPrice;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    copyHandler = new CopyHandler();
    copies = new ArrayList<>();
    _dataBinding();

    setPrice.setVisible(false);
    _displaySearchPanel();
    _eventHandlers();
    _displayCopies();
  }

  /**
   * Ajout des données au tableau des copies
   */
  private void _dataBinding() {
    setPrice.managedProperty().bind(setPrice.visibleProperty());
    resources.managedProperty().bind(resources.visibleProperty());
    colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));
  }

  /**
   * Gestion des évènements
   */
  @SuppressWarnings("unchecked")
  private void _eventHandlers() {
    // Tape sur "Enter" dans le champs de prix
    txtPrice.setOnAction((ActionEvent event) -> btnAdd.fire());

    // Click sur le boutton annuler
    btnCancel.setOnAction((ActionEvent event) -> _toggleView(true, false));

    // Click sur le bouton ajouter
    btnAdd.setOnAction((ActionEvent event) -> {
      try {
        double price = Double.parseDouble(txtPrice.getText());
        currentCopy.setPrice(price);
      } catch (NumberFormatException e) {
        Dialog.information("Vous devez entrer un montant valide");
        return;
      }

      currentCopy.setMember(member);
      currentCopy.setId(copyHandler.addCopy(currentCopy).getId());
      copies.add(currentCopy);

      if (currentCopy.getParent().getNo() != 0) {
        String title = "Reservation";
        String message = "Cet article a été réservé par " + currentCopy.getParent().getFirstName() + " " + currentCopy.getParent().getLastName() + " (" + currentCopy.getParent().getNo() + "). Veuillez le mettre dans la section des réservations parents-étudiants";
        Dialog.information(title, message);
      }

      currentCopy = null;
      _displayCopies();
      _toggleView(true, true);
    });

    // Click droit dans la liste d'copies
    tblCopies.setOnMouseClicked((MouseEvent event) -> {
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (copy != null && isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem supprimer = new MenuItem("Supprimer");
        contextMenu.getItems().addAll(supprimer);
        row.setContextMenu(contextMenu);

        // Clique sur le choix supprimer
        supprimer.setOnAction((ActionEvent event1) -> {
          if (copyHandler.deleteCopy(copy.getId())){
            copies.remove(copy);
            _displayCopies();
          }
        });
      }
    });

    memberName.setOnMouseClicked(event -> ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(getMember()));
  }

  @SuppressWarnings("unchecked")
  private void _searchEventHandlers() {
    SearchController searchController = (SearchController) controller;
    // Double click sur un item de la liste d'articles
    searchController.getTblItemResults().setOnMousePressed((MouseEvent event) -> {
      if (isDoubleClick(event)) {
        TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
        Item item = (Item) row.getItem();

        if (item != null) {
          _selectItem(item);
        }
      }
    });

    searchController.getTblItemResults().setOnKeyPressed(event -> {
      Item item = searchController.getTblItemResults().getSelectionModel().getSelectedItem();

      if (event.getCode().equals(KeyCode.ENTER) && item != null) {
        _selectItem(item);
        event.consume();
      }
    });

    // Ouvrir l'interface pour ajouter un nouvel item
    searchController.getBtnAdd().setOnAction((ActionEvent event) -> _displayItemForm());
  }

  private void _selectItem(Item item) {
    currentCopy = new Copy();
    currentCopy.setItem(item);
    itemTitle.setText(item.getName());

    _toggleView(true, false);
  }

  private void _itemFormEventHandlers() {
    ItemFormController itemFormController = (ItemFormController) controller;

    Button[] saveButtons = new Button[]{itemFormController.getBtnSaveBook(), itemFormController.getBtnSaveItem()};
    for (Button save : saveButtons) {
      save.setOnAction((ActionEvent event) -> {
        if (itemFormController.save()) {
          currentCopy = new Copy();
          currentCopy.setItem(itemFormController.getItem());
          itemTitle.setText(itemFormController.getItem().getName());

          _displaySearchPanel();
          _toggleView(true, true);
        }
      });
    }

    for (Button cancel : itemFormController.getCancelButtons()) {
      cancel.setOnAction((ActionEvent event) -> {
        _displaySearchPanel();
        event.consume();
      });
    }
  }

  /**
   * Ajouter les informations du member
   * auquel ont veux ajouter des copies
   * @param member Le member actif
   */
  public void loadMembre(Member member) {
    this.member = member;
    memberName.setText(this.member.getFirstName() + " " + this.member.getLastName());
  }

  /**
   * Rendre le member actif publique
   * @return Le member actif
   */
  public Member getMember() {
    return member;
  }

  /**
   * Affiche le panneau de recherche
   * @return Controller de recherche
   */
  private SearchController _displaySearchPanel() {
    controller = _loadPanel("layout/search.fxml");
    _searchEventHandlers();
    ((SearchController) controller).setParentController(this);
    ((SearchController) controller).setSearchItems();
    return (SearchController) controller;
  }

  private Controller _loadPanel(String resource) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(WindowController.class.getClassLoader().getResource(resource));
    resources.getChildren().clear();

    try {
      resources.getChildren().add(loader.load());
      return loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Recherche le panneau d'ajout d'item
   * @return Controller d'ajout d'articles
   */
  private ItemFormController _displayItemForm() {
    controller = _loadPanel("layout/itemForm.fxml");
    _itemFormEventHandlers();
    return (ItemFormController) controller;
  }

  /**
   * Refraichir l'affichage du tableau d'copies ajoutés
   */
  private void _displayCopies() {
    ObservableList<Copy> copiesList = FXCollections.observableArrayList(copies);
    tblCopies.setItems(copiesList);
    tblCopies.setVisible(!tblCopies.getItems().isEmpty());
  }

  /**
   * Toggle entre la view de recherche et d'ajout de prix
   * @param resetPrice S'il faut effacer le champs de prix
   * @param resetSearch S'il faut effacer les données de recherche
   */
  private void _toggleView(boolean resetPrice, boolean resetSearch) {
    if (resetPrice) {
     txtPrice.setText("");
    }

    if (resetSearch && controller instanceof SearchController) {
      ((SearchController) controller).resetSearch(true);
    }

    resources.setVisible(!resources.isVisible());
    setPrice.setVisible(!setPrice.isVisible());
  }

  @Override
  protected void handleScan(String code, boolean isItem) {
    if (isItem && currentCopy == null) {
      ItemHandler itemHandler = new ItemHandler();

      if (itemHandler.exists(code)) {
        currentCopy = new Copy();
        currentCopy.setItem(itemHandler.selectItem(code));
        itemTitle.setText(itemHandler.getItem().getName());

        _toggleView(true, false);
      } else {
        _displayItemForm().loadItem(code);
      }
    } else {
      Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
    }
  }
}
