/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import model.item.Copy;
import model.item.Item;
import handler.CopyHandler;
import model.member.Member;

import ressources.Dialog;

/**
 * Controller de l'interface d'ajout
 * d'copies dans un compte de member
 * gère aussi la recherche et l'ajout d'articles
 * @author Jessy
 * @since 28/03/2016
 * @version 1.0
 */
public class CopyFormController extends Controller {
  private Controller controller;
  private Pane panel;

  private CopyHandler copyHandler;
  private Member member;
  private ArrayList<Copy> copies;
  private Copy currentCopy;

  @FXML private AnchorPane ressources;
  @FXML private AnchorPane setPrice;
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
  }

  /**
   * Ajout des données au tableau des copies
   */
  private void _dataBinding() {
    colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
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
      currentCopy.setId(copyHandler.addCopy(currentCopy));
      copies.add(currentCopy);
      currentCopy = null;

      _displayCopies();
      _toggleView(true, true);
    });

    // Click droit dans la liste d'copies
    tblCopies.setOnMouseClicked((MouseEvent event) -> {
      Node node = ((Node) event.getTarget()).getParent();
      TableRow<Copy> row;

      if(node instanceof TableRow) {
        row = (TableRow<Copy>) node;
      } else {
        row = (TableRow<Copy>) node.getParent();
      }

      final Copy e = row.getItem();

      if(event.getButton() == MouseButton.SECONDARY) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem supprimer = new MenuItem("Supprimer");
        contextMenu.getItems().addAll(supprimer);
        row.setContextMenu(contextMenu);

        // Clique sur le choix supprimer
        supprimer.setOnAction((ActionEvent event1) -> {
          if (copyHandler.deleteCopy(e)){
            copies.remove(e);
            _displayCopies();
          }
        });
      }
    });
  }

  @SuppressWarnings("unchecked")
  private void _searchEventHandlers() {
    // Double click sur un item de la liste d'articles
    ((SearchController) controller).getTblItemResults().setOnMousePressed((MouseEvent event) -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow<Item> row;

        if (node instanceof TableRow) {
          row = (TableRow<Item>) node;
        } else {
          row = (TableRow<Item>) node.getParent();
        }

        Item a = row.getItem();
        currentCopy = new Copy();
        currentCopy.setItem(a);
        itemTitle.setText(a.getName());

        _toggleView(true, false);
      }
    });

    // Ouvrir l'interface pour ajouter un nouvel item
    ((SearchController) controller).getBtnAdd().setOnAction((ActionEvent event) -> _displayItemForm());
  }

  private void _itemFormEventHandlers() {
    ((ItemFormController) controller).getBtnAjoutOuvrage().setOnAction((ActionEvent event) -> {
      Item a = ((ItemFormController) controller).addItem();
      currentCopy = new Copy();
      currentCopy.setItem(a);
      itemTitle.setText(a.getName());

      _displaySearchPanel();
      _toggleView(true, true);
    });

    ((ItemFormController) controller).getBtnAjoutObjet().setOnAction((ActionEvent event) -> {
      Item a = ((ItemFormController) controller).addItem();
      currentCopy = new Copy();
      currentCopy.setItem(a);
      itemTitle.setText(a.getName());

      _displaySearchPanel();
      _toggleView(true, true);
    });
  }

  /**
   * Ajouter les informations du member
   * auquel ont veux ajouter des copies
   * @param m Le member actif
   */
  public void loadMembre(Member m) {
    member = m;
    memberName.setText(member.getFirstName() + " " + member.getLastName());
  }

  /**
   * Rendre le Label du nom du member publique
   * @return Le label du nom du member
   */
  public Label getMemberName() {
    return memberName;
  }

  /**
   * Rendre le member actif publique
   * @return Le member actif
   */
  @NotNull
  public Member getMember() {
    return member;
  }

  /**
   * Affiche le panneau de recherche
   * @return Controller de recherche
   */
  private SearchController _displaySearchPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/search.fxml"));
      panel = loader.load();
      controller = (SearchController) loader.getController();
      ressources.getChildren().clear();
      ressources.getChildren().add(panel);

      _searchEventHandlers();

      ((SearchController) controller).setSearchItems();
      return (SearchController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Recherche le panneau d'ajout d'item
   * @return Controller d'ajout d'articles
   */
  private ItemFormController _displayItemForm() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemForm.fxml"));
      panel = loader.load();
      controller = (ItemFormController) loader.getController();
      ressources.getChildren().clear();
      ressources.getChildren().add(panel);

      _itemFormEventHandlers();
      return (ItemFormController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Refraichir l'affichage du tableau d'copies ajoutés
   */
  private void _displayCopies() {
    ObservableList<Copy> copiesList = FXCollections.observableArrayList(copies);
    tblCopies.setItems(copiesList);

    tblCopies.setPrefHeight(50 * (copies.size() + 1));
    ressources.setLayoutY(150 + 50 * copies.size());
    setPrice.setLayoutY(150 + 50 * copies.size());
  }

  /**
   * Toggle entre la view de recherche et d'ajout de prix
   * @param resetPrix S'il faut effacer le champs de prix
   * @param resetRecherche S'il faut effacer les données de recherche
   */
  private void _toggleView(boolean resetPrix, boolean resetRecherche) {
    if (resetPrix) {
     txtPrice.setText("");
    }

    if (resetRecherche && controller instanceof SearchController) {
      ((SearchController) controller).resetSearch(true);
    }

    ressources.setVisible(!ressources.isVisible());
    setPrice.setVisible(!setPrice.isVisible());
  }
}
